package edu.tmu.group67.scrum_development.sprint.model.dto;

import edu.tmu.group67.scrum_development.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintDto {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;
    private int capacity;
    private boolean approved;
    private LocalDateTime createdAt;
}
