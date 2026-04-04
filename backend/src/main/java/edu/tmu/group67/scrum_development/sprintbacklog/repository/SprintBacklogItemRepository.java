package edu.tmu.group67.scrum_development.sprintbacklog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;

@Repository
public interface SprintBacklogItemRepository extends JpaRepository<SprintBacklogItemEntity, Long> {
    
}
