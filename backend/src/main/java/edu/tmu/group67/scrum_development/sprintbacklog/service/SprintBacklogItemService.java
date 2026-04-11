package edu.tmu.group67.scrum_development.sprintbacklog.service;

import java.time.LocalDateTime;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Service;

import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.productbacklog.repository.BacklogItemRepository;
import edu.tmu.group67.scrum_development.sprint.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;
import edu.tmu.group67.scrum_development.sprintbacklog.repository.SprintBacklogItemRepository;
import edu.tmu.group67.scrum_development.enums.Status;

@Service
@RequiredArgsConstructor
public class SprintBacklogItemService {
    private final SprintBacklogItemRepository sprintBacklogRepository;
    private final BacklogItemRepository backlogRepository;
    private final SprintRepository sprintRepository;

    public void addItemToSprint(Long sprintId, AddToSprintRequest request) {
        BacklogItemEntity item = backlogRepository.findById(request.backlogItemId()).orElseThrow();
        
        // Priority Constraint Logic
        boolean higherPriorityExists = backlogRepository.existsByStatusAndPriorityLessThan(
            Status.PLANNED, item.getPriority());
        
        if (higherPriorityExists) {
            throw new IllegalStateException("Must add all higher priority items first.");
        }

        SprintBacklogItemEntity sprintItem = SprintBacklogItemEntity.builder()
                .sprint(sprintRepository.getReferenceById(sprintId))
                .backlogItem(item)
                .plannedEffort(item.getEffort())
                .actualEffort(0)
                .status(Status.PLANNED)
                .addedAt(LocalDateTime.now())
                .build();
        
        sprintBacklogRepository.save(sprintItem);
    }
}
