package edu.tmu.group67.scrum_development.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.tasks.model.entity.EngineeringTaskEntity;

@Repository
public interface EngineeringTaskRepository extends JpaRepository<EngineeringTaskEntity, Long> {
    
}
