package edu.tmu.group67.scrum_development.tasks.controller;

import edu.tmu.group67.scrum_development.auth.model.entity.User;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.tasks.model.dto.EngineeringTaskDto;
import edu.tmu.group67.scrum_development.tasks.model.entity.EngineeringTaskEntity;
import edu.tmu.group67.scrum_development.tasks.service.EngineeringTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class EngineeringTaskController {

    private final EngineeringTaskService taskService;

    // GET /tasks/item/{sprintBacklogItemId}
    @GetMapping("/item/{sprintBacklogItemId}")
    public ResponseEntity<List<EngineeringTaskDto>> getTasksForItem(
            @PathVariable Long sprintBacklogItemId) {
        return ResponseEntity.ok(
                taskService.getTasksForItem(sprintBacklogItemId)
                        .stream().map(this::toDto).collect(Collectors.toList()));
    }

    // POST /tasks/item/{sprintBacklogItemId}
    @PostMapping("/item/{sprintBacklogItemId}")
    public ResponseEntity<?> createTask(
            @PathVariable Long sprintBacklogItemId,
            @RequestBody EngineeringTaskDto dto,
            @AuthenticationPrincipal User currentUser) {
        try {
            EngineeringTaskEntity created = taskService.createTask(
                    sprintBacklogItemId,
                    dto.getTitle(),
                    dto.getDescription() != null ? dto.getDescription() : "",
                    dto.getEffort() != null ? dto.getEffort() : 0,
                    currentUser);
            return ResponseEntity.ok(toDto(created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // PUT /tasks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestBody EngineeringTaskDto dto) {
        try {
            EngineeringTaskEntity updated = taskService.updateTask(
                    id,
                    dto.getTitle(),
                    dto.getDescription() != null ? dto.getDescription() : "",
                    dto.getEffort() != null ? dto.getEffort() : 0,
                    dto.getStatus() != null ? dto.getStatus() : Status.IN_PROGRESS);
            return ResponseEntity.ok(toDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // DELETE /tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    private EngineeringTaskDto toDto(EngineeringTaskEntity e) {
        return EngineeringTaskDto.builder()
                .id(e.getId())
                .sprintBacklogItemId(e.getSprintBacklogItem().getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .effort(e.getEffort())
                .status(e.getStatus())
                .createdById(e.getCreatedBy().getId())
                .build();
    }
}
