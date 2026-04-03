package edu.tmu.group67.scrum_development.sprintbacklog.model.entity;
// pkgs
import jakarta.persistence.*;
import java.time.LocalDateTime;

import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
//import edu.tmu.group67.scrum_development.auth.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "sprint_backlog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SprintBacklogItemEntity {
    // id (gen)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // sprint_id (big int) -> relation
    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn (name="sprint_id")
    private SprintEntity sprintId;

    // backlog_item_id (big int) -> relation
    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn (name="backlog_item_id")
    private BacklogItemEntity backlogItemId;
    
    // planned_effort(int)
    @Column(name = "planned_effort", nullable = false)
        private int plannedEffort;

    // actual_effort (int)
     @Column(name = "actual_effort", nullable = false)
        private int actualEffort;

    // locked (bool)
    @Column(nullable = false)
        private boolean locked;

    // added_at (datetime)
    @Column(name = "added_at")
    @Builder.Default
    private LocalDateTime addeddAt = LocalDateTime.now();

    // status (enum)
}
