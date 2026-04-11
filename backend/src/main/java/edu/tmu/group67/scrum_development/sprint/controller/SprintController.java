package edu.tmu.group67.scrum_development.sprint.controller;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.sprint.model.dto.SprintRequest;
import edu.tmu.group67.scrum_development.sprint.model.dto.SprintResponse;
import edu.tmu.group67.scrum_development.sprint.service.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/sprints")
@RequiredArgsConstructor
public class SprintController {
    private final SprintService sprintService;

    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping("/create")
    public ResponseEntity<SprintResponse> create(@RequestBody SprintRequest req) {
        return ResponseEntity.ok(sprintService.createSprint(req));
    }

    @PreAuthorize("hasRole('REPRESENTATIVE')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id, @RequestParam boolean accept) {
        sprintService.reviewProposal(id, accept);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasRole('DEVELOPER')")
    @PatchMapping("/items/{sprintBacklogId}")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long sprintBacklogId, 
            @RequestBody ProgressUpdate update) {
        sprintService.updateItemProgress(sprintBacklogId, update);
        return ResponseEntity.ok().build();
    }
    
}
