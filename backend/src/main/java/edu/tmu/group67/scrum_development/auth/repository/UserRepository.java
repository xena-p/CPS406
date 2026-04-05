package edu.tmu.group67.scrum_development.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.tmu.group67.scrum_development.auth.model.entity.User;
import java.util.Optional;

/* Repository interface for User entity, extends JpaRepository to provide CRUD operations like findById, findAll etc and you cna also define custom 
query methods here if needed. The @Repository annotation indicates that this interface is a Spring Data Repository, which will be automatically 
implemented by Spring Data JPA at runtime.

long bc it represents the primary key of the User entity, which is of type long and capital Long because it is a wrapper class that can be null, while 
long is a primitive type that cannot be null. Using Long allows for the possibility of a null value, which can be useful in certain situations, such as 
when a User entity has not yet been persisted to the database and therefore does not have an assigned id.
*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String token);
}
