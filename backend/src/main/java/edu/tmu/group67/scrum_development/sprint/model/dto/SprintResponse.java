package edu.tmu.group67.scrum_development.sprint.model.dto;

import java.time.LocalDateTime;

public record SprintResponse(
    Long id, 
    String name, 
    Integer capacity, 
    String status, 
    boolean isApproved, 
    LocalDateTime startDate, 
    LocalDateTime endDate) {}