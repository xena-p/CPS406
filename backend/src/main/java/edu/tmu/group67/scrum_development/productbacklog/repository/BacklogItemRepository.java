package edu.tmu.group67.scrum_development.productbacklog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;

@Repository
public interface BacklogItemRepository extends JpaRepository<BacklogItemEntity, Long> {
    
}
