package edu.tmu.group67.scrum_development.sprintbacklog.controller;

import edu.tmu.group67.scrum_development.sprintbacklog.model.dto.SprintBacklogItemDto;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;
import edu.tmu.group67.scrum_development.sprintbacklog.service.SprintBacklogItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sprint-backlog")
@RequiredArgsConstructor
public class SprintBacklogItemController {

    private final SprintBacklogItemService sprintBacklogItemService;

    // GET /sprint-backlog/{sprintId} — get all items for a sprint
    @GetMapping("/{sprintId}")
    public ResponseEntity<List<SprintBacklogItemDto>> getItemsForSprint(@PathVariable Long sprintId) {
        List<SprintBacklogItemDto> items = sprintBacklogItemService.getItemsForSprint(sprintId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    // POST /sprint-backlog/{sprintId}/{backlogItemId} — add a backlog item to a sprint
    @PostMapping("/{sprintId}/{backlogItemId}")
    public ResponseEntity<?> addItem(@PathVariable Long sprintId, @PathVariable Long backlogItemId) {
        try {
            SprintBacklogItemEntity saved = sprintBacklogItemService.addItemToSprint(sprintId, backlogItemId);
            return ResponseEntity.ok(toDto(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // PUT /sprint-backlog/{id}/done — mark a sprint backlog item as DONE
    @PutMapping("/{id}/done")
    public ResponseEntity<?> markDone(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toDto(sprintBacklogItemService.markItemDone(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // DELETE /sprint-backlog/{id} — remove an item from a sprint
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeItem(@PathVariable Long id) {
        try {
            sprintBacklogItemService.removeItemFromSprint(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    private SprintBacklogItemDto toDto(SprintBacklogItemEntity e) {
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
