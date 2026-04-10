package edu.tmu.group67.scrum_development.tasks.model.dto;

import edu.tmu.group67.scrum_development.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngineeringTaskDto {
    private Long id;
    private Long sprintBacklogItemId;
    private String title;
    private String description;
    private Integer effort;
    private Status status;
    private Long createdById;
}
