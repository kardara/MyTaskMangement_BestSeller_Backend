package bestseller.com.TaskMangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bestseller.com.TaskMangement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
