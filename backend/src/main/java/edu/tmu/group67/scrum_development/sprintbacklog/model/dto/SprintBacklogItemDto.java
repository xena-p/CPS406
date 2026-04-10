package edu.tmu.group67.scrum_development.sprintbacklog.model.dto;

import edu.tmu.group67.scrum_development.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintBacklogItemDto {
    private Long id;
    private Long sprintId;
    private Long backlogItemId;
    private int plannedEffort;
    private int actualEffort;
    private boolean locked;
    private Status status;
}
