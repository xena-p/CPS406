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
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
    public SprintEntity createSprint(@NonNull String name, LocalDateTime startDate, LocalDateTime endDate) {
        SprintEntity newSprint= SprintEntity.builder()
        .name(name)
        .startDate(startDate)
        .endDate(endDate)
        .status(Status.IN_PROGRESS)
        .isApproved(false)
        .build();
        return sprintRepository.save(newSprint);
    }

    // helper for approveSprint sprint 
    public void approveSprint (Long id){
        SprintEntity sprint= sprintRepository.findById(id).get();
        sprint.setApproved(true);
        sprintRepository.save(sprint);
    }
    //processSprintProposal(customerId, status, comments) -locks all items if approved -c
     public SprintEntity processSprintProposal(Long sprintId,Long customerId, boolean customerIN){
         SprintEntity sprint=sprintRepository.findById(sprintId).orElseThrow(null);
         User user=userRepository.findById(customerId).orElseThrow(null);

         sprint.setApproved(customerIN);

         if (customerIN==true){
            //find all backlog items + lock all backlog items
            approveSprint(sprintId);
            List<SprintBacklogItemEntity> sprintItems=sprintBacklogRepository.findBySprintId(sprintId);

            for(SprintBacklogItemEntity item : sprintItems){
                item.setLocked(true);
            }

            sprintBacklogRepository.saveAll(sprintItems); 
        }
        return sprintRepository.save(sprint);
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
    public SprintEntity completeSprint (@NonNull Long id){
        // check if sprint alr is real, if so, break
        SprintEntity sprint=sprintRepository.findById(id).orElseThrow(null);
        sprint.setStatus(Status.DONE);
        return sprintRepository.save(sprint);
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

}
