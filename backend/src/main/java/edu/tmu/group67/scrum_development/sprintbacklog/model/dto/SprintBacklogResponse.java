package edu.tmu.group67.scrum_development.sprintbacklog.model.dto;
import edu.tmu.group67.scrum_development.enums.Status;
public record SprintBacklogResponse(
        Long id,
        Long sprintId,
        Long backlogItemId,
        int plannedEffort,
        int actualEffort,
        boolean locked,
        Status status
) {}