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
    // Backlog item details included so frontend doesn't need a second request
    private String backlogItemTitle;
    private String backlogItemPriority;
    private int backlogItemEffort;
    private int plannedEffort;
    private int actualEffort;
    private boolean locked;
    private Status status;
}
