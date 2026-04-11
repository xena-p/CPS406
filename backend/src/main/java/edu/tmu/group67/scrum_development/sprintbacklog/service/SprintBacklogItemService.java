package edu.tmu.group67.scrum_development.sprintbacklog.service;

import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.productbacklog.repository.BacklogItemRepository;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
import edu.tmu.group67.scrum_development.sprint.repository.SprintRepository;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;
import edu.tmu.group67.scrum_development.sprintbacklog.repository.SprintBacklogItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SprintBacklogItemService {

    private final SprintBacklogItemRepository sprintBacklogItemRepository;
    private final SprintRepository sprintRepository;
    private final BacklogItemRepository backlogItemRepository;

    public List<SprintBacklogItemEntity> getItemsForSprint(Long sprintId) {
        return sprintBacklogItemRepository.findBySprintId_Id(sprintId);
    }

    public SprintBacklogItemEntity addItemToSprint(Long sprintId, Long backlogItemId) {
        SprintEntity sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));
        BacklogItemEntity backlogItem = backlogItemRepository.findById(backlogItemId)
                .orElseThrow(() -> new RuntimeException("Backlog item not found"));

        if (sprint.isApproved()) {
            throw new RuntimeException("Cannot modify an approved sprint");
        }
        if (backlogItem.getStatus() != Status.PLANNED) {
            throw new RuntimeException("Only PLANNED backlog items can be added to a sprint");
        }

        // Prevent duplicate entries
        boolean alreadyInSprint = sprintBacklogItemRepository.findBySprintId_Id(sprintId)
                .stream()
                .anyMatch(i -> i.getBacklogItemId().getId().equals(backlogItemId));
        if (alreadyInSprint) {
            throw new RuntimeException("Item is already in this sprint");
        }

        SprintBacklogItemEntity item = SprintBacklogItemEntity.builder()
                .sprintId(sprint)
                .backlogItemId(backlogItem)
                .plannedEffort(backlogItem.getEffort())
                .actualEffort(0)
                .locked(false)
                .status(Status.PLANNED)
                .build();

        return sprintBacklogItemRepository.save(item);
    }

    public SprintBacklogItemEntity markItemDone(Long id) {
        SprintBacklogItemEntity item = sprintBacklogItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint backlog item not found"));
        item.setStatus(Status.DONE);
        return sprintBacklogItemRepository.save(item);
    }

    public void removeItemFromSprint(Long sprintBacklogItemId) {
        SprintBacklogItemEntity item = sprintBacklogItemRepository.findById(sprintBacklogItemId)
                .orElseThrow(() -> new RuntimeException("Sprint backlog item not found"));

        if (item.isLocked()) {
            throw new RuntimeException("Cannot remove a locked item from an approved sprint");
        }

        sprintBacklogItemRepository.deleteById(sprintBacklogItemId);
    }
}
