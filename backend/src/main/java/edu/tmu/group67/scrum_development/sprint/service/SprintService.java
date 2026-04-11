package edu.tmu.group67.scrum_development.sprint.service;

import org.springframework.stereotype.Service;

import edu.tmu.group67.scrum_development.auth.model.entity.User;
import edu.tmu.group67.scrum_development.auth.repository.UserRepository;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.productbacklog.repository.BacklogItemRepository;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
import edu.tmu.group67.scrum_development.sprint.repository.SprintRepository;
import edu.tmu.group67.scrum_development.sprintbacklog.controller.SprintBacklogItemController;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;
import edu.tmu.group67.scrum_development.sprintbacklog.repository.SprintBacklogItemRepository;
import edu.tmu.group67.scrum_development.sprint.model.dto.SprintRequest;
import edu.tmu.group67.scrum_development.sprint.model.dto.SprintResponse;   
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import edu.tmu.group67.scrum_development.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.management.RuntimeErrorException;

//FXNS for sprint class
//@Builder
@Service
@RequiredArgsConstructor
public class SprintService {
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final BacklogItemRepository backlogItemRepository;
    private final SprintBacklogItemRepository sprintBacklogRepository;

    //createSprint (name, startDate, endDate)
    public SprintResponse createSprint(SprintRequest request) {
        if (sprintRepository.existsByStatus(Status.IN_PROGRESS)) {
            throw new IllegalStateException("Cannot initiate a new cycle while a sprint is active.");
        }
        
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime twoWeeksLater = today.plusWeeks(2);
        
        SprintEntity newSprint= SprintEntity.builder()
            .name(request.name())
            .capacity(request.capacity())
            .startDate(twoWeeksLater)
            .endDate(twoWeeksLater.plusWeeks(2))
            .status(Status.PLANNED)
            .isApproved(false)
            .build();
        newSprint = sprintRepository.save(newSprint);
        List<BacklogItemEntity> masterBacklog = backlogItemRepository
            .findByStatusOrderByPriority(Status.PLANNED);

        int  currentEffort = 0;
        for (BacklogItemEntity item : masterBacklog) {
            // Check if item fits in remaining capacity
            if (currentEffort + item.getEffort() <= request.capacity()) {
                // Link item to this sprint proposal
                SprintBacklogItemEntity sprintItem = SprintBacklogItemEntity.builder()
                        .sprint(newSprint)
                        .backlogItemId(item)
                        .plannedEffort(item.getEffort())
                        .actualEffort(0)
                        .locked(false)
                        .status(Status.PLANNED)
                        .build();
                
                sprintBacklogRepository.save(sprintItem);
                currentEffort += item.getEffort();
            }
        }
        return mapToResponse(newSprint);
    } 



    // helper for approveSprint sprint 
    public void approveSprint (Long id, boolean isApproved){
        SprintEntity sprint= sprintRepository.findById(id).get();
        if (isApproved) {
            sprint.setApproved(true);
            sprint.setStatus(Status.IN_PROGRESS);
            sprint.setStartDate(LocalDateTime.now());
        } else {
        // If rejected, discard proposal and items
        sprintRepository.delete(sprint);
        }
    }
    //processSprintProposal(customerId, status, comments) -locks all items if approved -c
     public SprintResponse processSprintProposal(Long sprintId, boolean customerIN){
         SprintEntity sprint=sprintRepository.findById(sprintId).orElseThrow(null);
         //User user=userRepository.findById(customerId).orElseThrow(null);

         sprint.setApproved(customerIN);

         if (customerIN==true){
            //find all backlog items + lock all backlog items;
            sprint.setStatus(Status.IN_PROGRESS);           
            List<SprintBacklogItemEntity> sprintItems=sprintBacklogRepository.findBySprint_Id(sprintId);

            for(SprintBacklogItemEntity item : sprintItems){
                item.setLocked(true);
            }

            sprintBacklogRepository.saveAll(sprintItems); 
        }
        return mapToResponse(sprintRepository.save(sprint));
    }   
     //generateSprintProposal(capacity)
    //modifySprintProposal(itemId, action) -action handles both add and remove

    //submitSprintProposalForApproval()
    public SprintEntity submitSprintProposalForApproval(Long id){
        SprintEntity sprint=sprintRepository.findById(id).orElseThrow(null);
        if (sprint.isApproved()){ // make sure -c and -d approve
            throw new RuntimeErrorException(null);
        }
        else{
            //which status deals with approvals?????
            //sprint.isApproved();
            return sprintRepository.save(sprint);
        }
    }

    public void addItemToProposal(Long sprintId, Long itemId) {
    SprintEntity sprint = sprintRepository.findById(sprintId).orElseThrow();
    BacklogItemEntity item = backlogItemRepository.findById(itemId).orElseThrow();

    // Constraint: Is there anything higher priority still in the product backlog?
    boolean higherPriorityExists = backlogItemRepository.existsByStatusAndPriorityLessThan(
            Status.PLANNED, item.getPriority());
            
    if (higherPriorityExists) {
        throw new IllegalArgumentException("You must add higher priority items first!");
    }

    addBacklogItemToSprint(sprint, item); }




    //startSprint (sprintid) - changes status -official start
    public SprintEntity startSprint (@NonNull Long id){
        // check if sprint alr is real, if so, break
        SprintEntity sprint=sprintRepository.findById(id).orElseThrow(null);
        if (sprint.isApproved()){ // make sure -c and -d approve
            throw new RuntimeErrorException(null);
        }
        else{
            //adjust status...inprog?
            sprint.setStatus(Status.IN_PROGRESS);
            return sprintRepository.save(sprint);
        }
    }

        //completeSprint (sprintid) - changes status
    public SprintResponse completeSprint (@NonNull Long id){
        // check if sprint alr is real, if so, break
        SprintEntity sprint=sprintRepository.findById(id).orElseThrow(null);
        for (SprintBacklogItemEntity sItem : sprint.getSprintItems()) {
        if (sItem.getStatus() == Status.DONE) {
            sItem.setLocked(true); // Lock completed items
        } else {
            // Move incomplete items back to product backlog
            BacklogItemEntity productItem = sItem.getBacklogItem();
            productItem.setStatus(Status.PLANNED);
            backlogItemRepository.save(productItem);
        }
        }
        sprint.setStatus(Status.DONE);
        return mapToResponse(sprintRepository.save(sprint));
    }

    // find by status -> see sprint repo. file
    //getSprints(status) -use for get current/past (status) sprints
    public List<SprintEntity> getSprints (Status status){
        return sprintRepository.findByStatus(status);
    }

    // list all sprints
    public List<SprintEntity> getAllSprints (){
        return sprintRepository.findAll();
    }

    private SprintResponse mapToResponse(SprintEntity entity) {
        return new SprintResponse(
                entity.getId(),
                entity.getName(),
                entity.getCapacity(),
                entity.getStatus().name(), // Converts Enum to String
                entity.isApproved(),
                entity.getStartDate(),      // Ensure your SprintResponse has these
                entity.getEndDate()
        );
    }
}