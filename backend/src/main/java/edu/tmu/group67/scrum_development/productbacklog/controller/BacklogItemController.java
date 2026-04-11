package edu.tmu.group67.scrum_development.productbacklog.controller;
import edu.tmu.group67.scrum_development.productbacklog.model.dto.*;
import edu.tmu.group67.scrum_development.productbacklog.service.BacklogItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.tmu.group67.scrum_development.auth.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backlog")
@RequiredArgsConstructor // Lombok will generate a constructor with required arguments (final fields)
public class BacklogItemController {
    private final BacklogItemService service;

    @GetMapping
    public ResponseEntity<List<BacklogItemResponse>> getBacklog() {
        return ResponseEntity.ok(service.getAllBacklogItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BacklogItemResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getBacklogItem(id));
    }


    @PreAuthorize("hasAnyRole('DEVELOPER', 'CUSTOMER')")
    @PostMapping
    public ResponseEntity<BacklogItemResponse> createItem(
            @RequestBody @Valid BacklogItemRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createBacklogItem(request, currentUser));
    }

    @PreAuthorize("hasAnyRole('DEVELOPER', 'CUSTOMER')")
    @PatchMapping("/{id}")
    public ResponseEntity<BacklogItemResponse> updateBacklogItem(
            @PathVariable Long id,
            @RequestBody BacklogItemRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(service.updateBacklogItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        service.deleteBacklogItem(id);
        return ResponseEntity.noContent().build();
    }
}

