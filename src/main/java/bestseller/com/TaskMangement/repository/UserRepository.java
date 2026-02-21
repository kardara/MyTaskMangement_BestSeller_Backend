package bestseller.com.TaskMangement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bestseller.com.TaskMangement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
}
