package edu.tmu.group67.scrum_development.analytics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {
    private Long sprintId;
    private String sprintName;
    private int totalItems;
    private int completedItems;
    private int totalPlannedEffort;
    private int totalActualEffort;
    private List<Map<String, Object>> burndownData;
}
