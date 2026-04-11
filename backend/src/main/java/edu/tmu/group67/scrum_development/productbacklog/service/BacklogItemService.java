package edu.tmu.group67.scrum_development.productbacklog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.productbacklog.repository.BacklogItemRepository;
import edu.tmu.group67.scrum_development.sprintbacklog.repository.SprintBacklogItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Service
@RequiredArgsConstructor
public class BacklogItemService {

    private final BacklogItemRepository backlogItemRepository;
    private final SprintBacklogItemRepository sprintBacklogItemRepository;

    public BacklogItemEntity createBacklogItem(@NonNull BacklogItemEntity item){
        return backlogItemRepository.save(item);
    }

    public BacklogItemEntity updateBacklogItem(@NonNull Long id, @NonNull BacklogItemEntity updatedItem){
        BacklogItemEntity existingItem = backlogItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Backlog item not found with id: " + id));

        if (existingItem.getStatus() != Status.PLANNED) {
            throw new RuntimeException("Only PLANNED items can be modified");
        }

        existingItem.setTitle(updatedItem.getTitle());
        existingItem.setRequirements(updatedItem.getRequirements());
        existingItem.setStory(updatedItem.getStory());
        existingItem.setEffort(updatedItem.getEffort());
        existingItem.setPriority(updatedItem.getPriority());
        existingItem.setStatus(updatedItem.getStatus());
        existingItem.setRisk(updatedItem.getRisk());
        existingItem.setUpdateddAt(LocalDateTime.now());

        return backlogItemRepository.save(existingItem);
    }

    public void deleteBacklogItem(@NonNull Long id){
        BacklogItemEntity item = backlogItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Backlog item not found with id: " + id));

        if (item.getStatus() != Status.PLANNED) {
            throw new RuntimeException("Only PLANNED items can be deleted");
        }

        // Remove any sprint backlog references before deleting
        sprintBacklogItemRepository.deleteAll(
            sprintBacklogItemRepository.findByBacklogItemId_Id(id)
        );

        backlogItemRepository.deleteById(id);
    }

    public BacklogItemEntity getBacklogItem(@NonNull Long id){
        return backlogItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Backlog item not found with id: " + id));
    }

    public List<BacklogItemEntity> getAllBacklogItems(){
        return backlogItemRepository.findAll();
    }
}
