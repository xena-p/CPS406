package edu.tmu.group67.scrum_development.sprint.model.entity;

import edu.tmu.group67.scrum_development.enums.Status;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity //means it represents a table in the database
@Table(name = "sprints") //table name in the database
@Data //lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor //lombok annotation to generate a no-argument constructor
@AllArgsConstructor //lombok annotation to generate a constructor with all fields
@Builder

public class SprintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int capacity;

    @Column(name = "is_approved")
    @Builder.Default
    private boolean isApproved=false;


    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();




}
