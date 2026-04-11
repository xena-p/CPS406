package edu.tmu.group67.scrum_development.sprint.service;

import org.springframework.stereotype.Service;

import edu.tmu.group67.scrum_development.enums.Level;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.productbacklog.repository.BacklogItemRepository;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
import edu.tmu.group67.scrum_development.sprint.repository.SprintRepository;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;
import edu.tmu.group67.scrum_development.sprintbacklog.repository.SprintBacklogItemRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SprintService {
    private final SprintRepository sprintRepository;
    private final BacklogItemRepository backlogItemRepository;
    private final SprintBacklogItemRepository sprintBacklogRepository;

    // Creates a new sprint proposal with PLANNED status
    public SprintEntity createSprint(@NonNull String name, LocalDateTime startDate, LocalDateTime endDate, int capacity) {
        List<SprintEntity> activeSprints = sprintRepository.findByStatus(Status.ACTIVE);
        if (!activeSprints.isEmpty()) {
            throw new RuntimeException("A sprint is already active. Complete it before starting a new one.");
        }

        SprintEntity newSprint = SprintEntity.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .status(Status.PLANNED)
                .capacity(capacity)
                .isApproved(false)
                .build();
        return sprintRepository.save(newSprint);
    }

    // Auto-selects highest-priority PLANNED items that fit within the sprint's capacity
    public List<SprintBacklogItemEntity> generateSprintProposal(Long sprintId) {
        SprintEntity sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        if (sprint.isApproved()) {
            throw new RuntimeException("Cannot regenerate proposal for an approved sprint");
        }

        // Clear any previously generated items for this sprint
        List<SprintBacklogItemEntity> existing = sprintBacklogRepository.findBySprintId_Id(sprintId);
        sprintBacklogRepository.deleteAll(existing);

        // Fetch PLANNED items sorted by priority HIGH → MEDIUM → LOW
        List<BacklogItemEntity> plannedItems = backlogItemRepository.findByStatus(Status.PLANNED)
                .stream()
                .sorted(Comparator.comparingInt(item -> priorityOrder(item.getPriority())))
                .toList();

        List<SprintBacklogItemEntity> proposalItems = new ArrayList<>();
        int remainingCapacity = sprint.getCapacity();

        for (BacklogItemEntity item : plannedItems) {
            if (item.getEffort() <= remainingCapacity) {
                proposalItems.add(SprintBacklogItemEntity.builder()
                        .sprintId(sprint)
                        .backlogItemId(item)
                        .plannedEffort(item.getEffort())
                        .actualEffort(0)
                        .locked(false)
                        .status(Status.PLANNED)
                        .build());
                remainingCapacity -= item.getEffort();
            }
        }

        return sprintBacklogRepository.saveAll(proposalItems);
    }

    // Customer rep approves or rejects a sprint proposal
    public SprintEntity processSprintProposal(Long sprintId, boolean approved) {
        SprintEntity sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        if (approved) {
            sprint.setApproved(true);
            sprint.setStatus(Status.ACTIVE);
            sprintRepository.save(sprint);

            // Lock all sprint backlog items and set them to IN_PROGRESS
            List<SprintBacklogItemEntity> items = sprintBacklogRepository.findBySprintId_Id(sprintId);
            for (SprintBacklogItemEntity item : items) {
                item.setLocked(true);
                item.setStatus(Status.IN_PROGRESS);
            }
            sprintBacklogRepository.saveAll(items);
        } else {
            // Rejected — delete sprint backlog items then the sprint
            List<SprintBacklogItemEntity> items = sprintBacklogRepository.findBySprintId_Id(sprintId);
            sprintBacklogRepository.deleteAll(items);
            sprintRepository.delete(sprint);
            return null;
        }

        return sprint;
    }

    // Marks a sprint as complete
    public SprintEntity completeSprint(@NonNull Long id) {
        SprintEntity sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));
        sprint.setStatus(Status.DONE);
        return sprintRepository.save(sprint);
    }

    public List<SprintEntity> getSprints(Status status) {
        return sprintRepository.findByStatus(status);
    }

    public List<SprintEntity> getAllSprints() {
        return sprintRepository.findAll();
    }

    private int priorityOrder(Level priority) {
        if (priority == null) return 99;
        return switch (priority) {
            case HIGH -> 0;
            case MEDIUM -> 1;
            case LOW -> 2;
        };
    }
}
