package edu.tmu.group67.scrum_development.tasks.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
import edu.tmu.group67.scrum_development.auth.model.entity.User;


@Entity //means it represents a table in the database
@Table(name = "engineering_tasks") //table name in the database
@Data //lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor //lombok annotation to generate a no-argument constructor
@AllArgsConstructor //lombok annotation to generate a constructor with all fields
@Builder

public class EngineeringTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //sprint backlog item id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_backlog_item_id", nullable = false)
    private SprintEntity sprintBacklogItem;

    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "effort_estimate")
    private Integer effort;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    //
    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn (name="created_by", nullable = false)     // use @joincolumn for relationship
    private User createdBy;


}

