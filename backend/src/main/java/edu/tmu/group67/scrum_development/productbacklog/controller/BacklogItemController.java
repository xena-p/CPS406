package edu.tmu.group67.scrum_development.productbacklog.controller;

import edu.tmu.group67.scrum_development.auth.model.entity.User;
import edu.tmu.group67.scrum_development.productbacklog.model.dto.BacklogItemDto;
import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.productbacklog.service.BacklogItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backlog")
@RequiredArgsConstructor
public class BacklogItemController {

    private final BacklogItemService backlogItemService;

    // GET /backlog
    @GetMapping
    public ResponseEntity<List<BacklogItemDto>> getAllItems() {
        List<BacklogItemDto> items = backlogItemService.getAllBacklogItems()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    // GET /backlog/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BacklogItemDto> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(backlogItemService.getBacklogItem(id)));
    }

    // POST /backlog — createdBy is taken from the JWT, not the request body
    @PostMapping
    public ResponseEntity<BacklogItemDto> createItem(
            @RequestBody BacklogItemDto dto,
            @AuthenticationPrincipal User currentUser) {
        BacklogItemEntity saved = backlogItemService.createBacklogItem(toEntity(dto, currentUser));
        return ResponseEntity.ok(toDto(saved));
    }

    // PUT /backlog/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody BacklogItemDto dto) {
        try {
            BacklogItemEntity updated = backlogItemService.updateBacklogItem(id, toEntity(dto, null));
            return ResponseEntity.ok(toDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // DELETE /backlog/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            backlogItemService.deleteBacklogItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // --- Mapping helpers ---

    private BacklogItemDto toDto(BacklogItemEntity e) {
        return BacklogItemDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .requirements(e.getRequirements())
                .story(e.getStory())
                .effort(e.getEffort())
                .createdById(e.getCreatedBy() != null ? e.getCreatedBy().getId() : null)
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdateddAt())
                .status(e.getStatus())
                .priority(e.getPriority())
                .risk(e.getRisk())
                .build();
    }

    private BacklogItemEntity toEntity(BacklogItemDto dto, User createdBy) {
        return BacklogItemEntity.builder()
                .title(dto.getTitle())
                .requirements(dto.getRequirements())
                .story(dto.getStory())
                .effort(dto.getEffort())
                .createdBy(createdBy)
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .risk(dto.getRisk())
                .build();
    }
}
