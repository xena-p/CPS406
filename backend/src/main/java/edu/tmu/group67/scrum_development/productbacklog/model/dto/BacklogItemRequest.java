package edu.tmu.group67.scrum_development.productbacklog.model.dto;
import edu.tmu.group67.scrum_development.enums.Level;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import edu.tmu.group67.scrum_development.enums.Status;

public record BacklogItemRequest (
    @NotBlank @Size(max = 255) String title,
    @NotBlank @Size(max = 300) String requirements,
    @NotBlank @Size(max = 300) String story,
    Integer effort, //supports null which is needed for patch 
    Level priority,
    Level risk,
    Status status
) {}
