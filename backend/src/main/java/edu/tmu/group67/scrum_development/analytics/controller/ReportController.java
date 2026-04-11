package edu.tmu.group67.scrum_development.analytics.controller;

import edu.tmu.group67.scrum_development.analytics.model.dto.ReportDto;
import edu.tmu.group67.scrum_development.analytics.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // GET /report — summary report for all sprints
    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    // GET /report/{sprintId} — detailed report for a single sprint
    @GetMapping("/{sprintId}")
    public ResponseEntity<?> getReport(@PathVariable Long sprintId) {
        try {
            return ResponseEntity.ok(reportService.generateReport(sprintId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
