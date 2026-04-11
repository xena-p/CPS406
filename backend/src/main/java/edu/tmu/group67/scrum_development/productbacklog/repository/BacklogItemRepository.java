package edu.tmu.group67.scrum_development.productbacklog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.productbacklog.model.entity.BacklogItemEntity;
import edu.tmu.group67.scrum_development.enums.Status;
import java.util.List;
@Repository
public interface BacklogItemRepository extends JpaRepository<BacklogItemEntity, Long> {
    boolean existsByTitle(String title);

    // Used for validation during updates
    boolean existsByTitleAndIdNot(String title, Long id);
    List<BacklogItemEntity> findByStatusOrderByPriority(Status status);

}
