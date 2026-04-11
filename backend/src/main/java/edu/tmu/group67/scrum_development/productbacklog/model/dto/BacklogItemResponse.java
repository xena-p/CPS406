package edu.tmu.group67.scrum_development.productbacklog.model.dto;
import java.time.LocalDateTime;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.enums.Level;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Builder;
@Builder
public record BacklogItemResponse (
    Long id,
    @NotBlank @Size(max = 255) String title,
    @NotBlank @Size(max = 300) String requirements,
    @NotBlank @Size(max = 300) String story,
    int effort, 
    Status status,
    Level priority,
    Level risk,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long createdById
) {}
