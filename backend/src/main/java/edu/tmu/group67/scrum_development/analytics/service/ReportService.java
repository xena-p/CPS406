package edu.tmu.group67.scrum_development.analytics.service;

import edu.tmu.group67.scrum_development.analytics.model.dto.ReportDto;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
import edu.tmu.group67.scrum_development.sprint.repository.SprintRepository;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;
import edu.tmu.group67.scrum_development.sprintbacklog.repository.SprintBacklogItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SprintRepository sprintRepository;
    private final SprintBacklogItemRepository sprintBacklogItemRepository;

    public ReportDto generateReport(Long sprintId) {
        SprintEntity sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        List<SprintBacklogItemEntity> items = sprintBacklogItemRepository.findBySprintId_Id(sprintId);

        int totalItems = items.size();
        int completedItems = (int) items.stream().filter(i -> i.getStatus() == Status.DONE).count();
        int totalPlannedEffort = items.stream().mapToInt(SprintBacklogItemEntity::getPlannedEffort).sum();
        int totalActualEffort = items.stream().mapToInt(SprintBacklogItemEntity::getActualEffort).sum();

        // Ideal burndown: linear from totalPlannedEffort down to 0 over 10 days
        // Actual burndown: effort remaining based on completed items so far
        int completedEffort = items.stream()
                .filter(i -> i.getStatus() == Status.DONE)
                .mapToInt(SprintBacklogItemEntity::getPlannedEffort)
                .sum();
        int remainingEffort = totalPlannedEffort - completedEffort;

        List<Map<String, Object>> burndownData = new ArrayList<>();
        for (int day = 0; day <= 10; day++) {
            Map<String, Object> point = new HashMap<>();
            point.put("day", day);
            // Ideal: decreases linearly
            point.put("ideal", totalPlannedEffort - (totalPlannedEffort * day / 10));
            // Actual: only plot current remaining at the proportional day based on completion
            if (totalItems > 0) {
                double completionRatio = (double) completedItems / totalItems;
                int actualDay = (int) Math.round(completionRatio * 10);
                if (day == actualDay) {
                    point.put("actual", remainingEffort);
                }
            }
            burndownData.add(point);
        }
        // Always mark day 0 actual as full effort
        burndownData.get(0).put("actual", totalPlannedEffort);

        return ReportDto.builder()
                .sprintId(sprint.getId())
                .sprintName(sprint.getName())
                .totalItems(totalItems)
                .completedItems(completedItems)
                .totalPlannedEffort(totalPlannedEffort)
                .totalActualEffort(totalActualEffort)
                .burndownData(burndownData)
                .build();
    }

    public List<ReportDto> getAllReports() {
        return sprintRepository.findAll().stream()
                .map(s -> generateReport(s.getId()))
                .collect(Collectors.toList());
    }
}
