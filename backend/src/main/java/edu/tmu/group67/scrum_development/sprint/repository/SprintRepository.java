package edu.tmu.group67.scrum_development.sprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;

@Repository
public interface SprintRepository extends JpaRepository<SprintEntity, Long> {
    
}
