package edu.tmu.group67.scrum_development.productbacklog.model.dto;

import edu.tmu.group67.scrum_development.enums.Level;
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
public class BacklogItemDto {
    private Long id;
    private String title;
    private String requirements;
    private String story;
    private int effort;
    private Long createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;
    private Level priority;
    private Level risk;
}
