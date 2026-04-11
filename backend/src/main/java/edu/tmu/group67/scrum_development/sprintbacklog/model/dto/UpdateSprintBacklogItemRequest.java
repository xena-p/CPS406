package edu.tmu.group67.scrum_development.sprintbacklog.model.dto;
import edu.tmu.group67.scrum_development.enums.Status;

public record UpdateSprintBacklogItemRequest(
        Integer actualEffort,
        Status status
) {}