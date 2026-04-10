package edu.tmu.group67.scrum_development.sprint.controller;

import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.sprint.model.dto.SprintDto;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
import edu.tmu.group67.scrum_development.sprint.service.SprintService;
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

    // GET /sprint
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

    // POST /sprint
    @PostMapping
    public ResponseEntity<SprintDto> createSprint(@RequestBody SprintDto dto) {
        SprintEntity created = sprintService.createSprint(dto.getName(), dto.getStartDate(), dto.getEndDate());
        return ResponseEntity.ok(toDto(created));
    }

    // PUT /sprint/{id}/submit - developer submits proposal for customer review
    @PutMapping("/{id}/submit")
    public ResponseEntity<SprintDto> submitProposal(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(sprintService.submitSprintProposalForApproval(id)));
    }

    // PUT /sprint/{id}/process?customerId=1&approved=true - customer rep approves or rejects
    @PutMapping("/{id}/process")
    public ResponseEntity<SprintDto> processProposal(
            @PathVariable Long id,
            @RequestParam Long customerId,
            @RequestParam boolean approved) {
        return ResponseEntity.ok(toDto(sprintService.processSprintProposal(id, customerId, approved)));
    }

    // PUT /sprint/{id}/start
    @PutMapping("/{id}/start")
    public ResponseEntity<SprintDto> startSprint(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(sprintService.startSprint(id)));
    }

    // PUT /sprint/{id}/complete
    @PutMapping("/{id}/complete")
    public ResponseEntity<SprintDto> completeSprint(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(sprintService.completeSprint(id)));
    }

    // --- Mapping helper ---

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
}
