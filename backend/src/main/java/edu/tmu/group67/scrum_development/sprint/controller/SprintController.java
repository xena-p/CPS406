package edu.tmu.group67.scrum_development.sprint.controller;

import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.sprint.model.dto.SprintDto;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
import edu.tmu.group67.scrum_development.sprint.service.SprintService;
import edu.tmu.group67.scrum_development.sprintbacklog.model.dto.SprintBacklogItemDto;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sprint")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    // GET /sprint — all sprints
    @GetMapping
    public ResponseEntity<List<SprintDto>> getAllSprints() {
        return ResponseEntity.ok(
                sprintService.getAllSprints().stream().map(this::toDto).collect(Collectors.toList()));
    }

    // GET /sprint/filter?status=ACTIVE
    @GetMapping("/filter")
    public ResponseEntity<List<SprintDto>> getSprintsByStatus(@RequestParam Status status) {
        return ResponseEntity.ok(
                sprintService.getSprints(status).stream().map(this::toDto).collect(Collectors.toList()));
    }

    // POST /sprint — create a new sprint proposal
    @PostMapping
    public ResponseEntity<?> createSprint(@RequestBody SprintDto dto) {
        try {
            SprintEntity created = sprintService.createSprint(
                    dto.getName(), dto.getStartDate(), dto.getEndDate(), dto.getCapacity());
            return ResponseEntity.ok(toDto(created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // POST /sprint/{id}/generate — auto-generate sprint backlog items from highest-priority PLANNED items
    @PostMapping("/{id}/generate")
    public ResponseEntity<List<SprintBacklogItemDto>> generateProposal(@PathVariable Long id) {
        List<SprintBacklogItemDto> items = sprintService.generateSprintProposal(id)
                .stream()
                .map(this::toSprintBacklogDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    // PUT /sprint/{id}/process?approved=true — customer rep approves or rejects
    @PutMapping("/{id}/process")
    public ResponseEntity<?> processProposal(
            @PathVariable Long id,
            @RequestParam boolean approved) {
        try {
            SprintEntity result = sprintService.processSprintProposal(id, approved);
            if (result == null) {
                return ResponseEntity.ok("Sprint rejected and removed.");
            }
            return ResponseEntity.ok(toDto(result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // PUT /sprint/{id}/complete
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeSprint(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toDto(sprintService.completeSprint(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // --- Mapping helpers ---

    private SprintDto toDto(SprintEntity e) {
        return SprintDto.builder()
                .id(e.getId())
                .name(e.getName())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .status(e.getStatus())
                .capacity(e.getCapacity())
                .approved(e.isApproved())
                .createdAt(e.getCreatedAt())
                .build();
    }

    private SprintBacklogItemDto toSprintBacklogDto(SprintBacklogItemEntity e) {
        return SprintBacklogItemDto.builder()
                .id(e.getId())
                .sprintId(e.getSprintId().getId())
                .backlogItemId(e.getBacklogItemId().getId())
                .backlogItemTitle(e.getBacklogItemId().getTitle())
                .backlogItemPriority(e.getBacklogItemId().getPriority() != null
                        ? e.getBacklogItemId().getPriority().name() : null)
                .backlogItemEffort(e.getBacklogItemId().getEffort())
                .plannedEffort(e.getPlannedEffort())
                .actualEffort(e.getActualEffort())
                .locked(e.isLocked())
                .status(e.getStatus())
                .build();
    }
}
