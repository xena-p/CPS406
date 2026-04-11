package edu.tmu.group67.scrum_development.sprintbacklog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;

@Repository
public interface SprintBacklogItemRepository extends JpaRepository<SprintBacklogItemEntity, Long> {
    // sprintId field in the entity is a SprintEntity (ManyToOne), so we traverse to its id
    List<SprintBacklogItemEntity> findBySprintId_Id(Long sprintId);
    List<SprintBacklogItemEntity> findByBacklogItemId_Id(Long backlogItemId);
}
