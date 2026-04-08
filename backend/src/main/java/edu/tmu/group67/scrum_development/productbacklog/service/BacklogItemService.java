package edu.tmu.group67.scrum_development.productbacklog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.productbacklog.repository.BacklogItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Service
@RequiredArgsConstructor
public class BacklogItemService {
// fxns for prod backlog
// helpful docs: 
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html


    //set up the repo needed for the services
    private final BacklogItemRepository backlogItemRepository;

    //createBacklogItem (title, description, priority, effort, createdBy) - c
    public BacklogItemEntity createBacklogItem(@NonNull BacklogItemEntity item){
        // @NonNull to prevent null entries.
            // pass entity so ctrl can get json for frontend
        return backlogItemRepository.save(item);
    }

    //updateBacklogItem (id, fieldsToUpdate)
    public BacklogItemEntity updateBacklogItem (@NonNull Long id,@NonNull BacklogItemEntity updatedItem){
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
    BacklogItemEntity existingItem=backlogItemRepository.findById(id).orElseThrow(null));
    //setting

    //text data
    existingItem.setTitle(updatedItem.getTitle());
    existingItem.setRequirements(updatedItem.getRequirements());
    existingItem.setStory(updatedItem.getStory());
    //num data
    existingItem.setEffort(updatedItem.getEffort());
    existingItem.setPriority(updatedItem.getPriority());
    existingItem.setStatus(updatedItem.getStatus());
    existingItem.setRisk(updatedItem.getRisk());

    // time of edit
    existingItem.setUpdateddAt(LocalDateTime.now());
    //save
    return backlogItemRepository.save(existingItem);
    }

    //deleteBacklogItem (id) - returns none
    public void deleteBacklogItem (@NonNull Long id){
        // built in del by id 
        backlogItemRepository.deleteById(id);
    }

    //retrive backlog itm (singular)
    public BacklogItemEntity getBacklogItem (@NonNull Long id){
        return backlogItemRepository.findById(id).orElseThrow(null);
    }

    //getAllBacklogItems ()
    public List<BacklogItemEntity> getAllBacklogItems (){
        return backlogItemRepository.findAll();
    }
    
}
