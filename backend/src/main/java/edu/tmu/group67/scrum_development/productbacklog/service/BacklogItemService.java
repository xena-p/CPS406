package edu.tmu.group67.scrum_development.productbacklog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import edu.tmu.group67.scrum_development.auth.model.entity.User;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.productbacklog.model.dto.BacklogItemRequest;
import edu.tmu.group67.scrum_development.productbacklog.model.dto.BacklogItemResponse;
import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.productbacklog.repository.BacklogItemRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import lombok.NonNull;

@Service
@Validated
@RequiredArgsConstructor
public class BacklogItemService {
// fxns for prod backlog
// helpful docs: 
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html


    //set up the repo needed for the services
    private final BacklogItemRepository backlogItemRepository;

    //createBacklogItem (title, description, priority, effort, createdBy) - c
    public BacklogItemResponse createBacklogItem(@Valid BacklogItemRequest request, User currentUser){
        // @NonNull to prevent null entries.
            // pass entity so ctrl can get json for frontend
        if(backlogItemRepository.existsByTitle(request.title())) {
            throw new RuntimeException("An item with this title already exists.");
        }
        BacklogItemEntity item = BacklogItemEntity.builder()
                .title(request.title())
                .requirements(request.requirements())
                .story(request.story())
                .effort(request.effort())
                .priority(request.priority())
                .risk(request.risk())
                .status(Status.PLANNED)
                .createdBy(currentUser)
                .build();
        return mapToResponse(backlogItemRepository.save(item));
    }

    //updateBacklogItem (id, fieldsToUpdate)
    @Transactional
    public BacklogItemResponse updateBacklogItem (@NonNull Long id, BacklogItemRequest updatedItem){
    /*example implementation
    https://www.baeldung.com/spring-data-jpa-refresh-fetch-entity-after-save
    // 1. FETCH
    const user = await db.users.findById(id);
    // 2. MAP (Merge)
    // incomingData = { "email": "new@example.com" }
    Object.assign(user, incomingData);
    // 3. SAVE
    await db.users.save(user); 
    */
    BacklogItemEntity existingItem=backlogItemRepository.findById(id).orElseThrow(null);
    //setting

    //ONLY planned items can be edited, so check status first
    if (existingItem.getStatus() != Status.PLANNED) {
            throw new IllegalStateException("Cannot edit items that are already in progress or done.");
    }

    // if title is being updated, check for uniqueness
    if (backlogItemRepository.existsByTitleAndIdNot(updatedItem.title(), id)) {
            throw new RuntimeException("Another item already has this title");
    }



    //text data
    if (updatedItem.title() != null) {
        existingItem.setTitle(updatedItem.title());
    }
    if (updatedItem.requirements() != null) {
        existingItem.setRequirements(updatedItem.requirements());
    }
    if (updatedItem.story() != null) {
        existingItem.setStory(updatedItem.story());
    }
    //num data
    if (updatedItem.effort() != null) {
        existingItem.setEffort(updatedItem.effort());
    }
    if (updatedItem.priority() != null) {
        existingItem.setPriority(updatedItem.priority());
    }
    if (updatedItem.status() != null) {
        existingItem.setStatus(updatedItem.status());
    }
    if (updatedItem.risk() != null) {
        existingItem.setRisk(updatedItem.risk());
    }

    // time of edit
    existingItem.setUpdatedAt(LocalDateTime.now());
    //save
    return mapToResponse(backlogItemRepository.save(existingItem));
    }

    //deleteBacklogItem (id) - returns none
    public void deleteBacklogItem (@NonNull Long id){
        BacklogItemEntity item = backlogItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getStatus() != Status.PLANNED) {
            throw new IllegalStateException("Only 'planned' items can be deleted.");
        }
        
        // built in del by id 
        backlogItemRepository.deleteById(id);
    }

    //retrive backlog itm (singular)
    public BacklogItemResponse getBacklogItem (@NonNull Long id){
        return backlogItemRepository.findById(id)
            .map(this::mapToResponse)
            .orElseThrow(() -> new RuntimeException("Backlog item with ID " + id + " not found"));
}

    //getAllBacklogItems ()
    public List<BacklogItemResponse> getAllBacklogItems (){
        return backlogItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }
    

    private BacklogItemResponse mapToResponse(BacklogItemEntity item) {
        return BacklogItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .requirements(item.getRequirements())
                .story(item.getStory())
                .effort(item.getEffort())
                .status(item.getStatus())
                .priority(item.getPriority())
                .risk(item.getRisk())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .createdById(item.getCreatedBy() != null ? item.getCreatedBy().getId() : null)
                .build();
    }
}
