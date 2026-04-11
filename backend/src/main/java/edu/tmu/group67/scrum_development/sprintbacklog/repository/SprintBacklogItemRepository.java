package edu.tmu.group67.scrum_development.sprintbacklog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;

@Repository
public interface SprintBacklogItemRepository extends JpaRepository<SprintBacklogItemEntity, Long> {
    List<SprintBacklogItemEntity> findBySprintId(Long sprintId);
        boolean existsBySprintIdAndBacklogItemId_Id(Long sprintId, Long backlogItemId);
}

