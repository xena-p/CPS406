package edu.tmu.group67.scrum_development.sprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.sprint.model.entity.SprintEntity;
import edu.tmu.group67.scrum_development.enums.Status;
import java.util.List;
@Repository
public interface SprintRepository extends JpaRepository<SprintEntity, Long> {
    List<SprintEntity> findByStatus(Status status);
    boolean existsByStatus(Status status);
}
