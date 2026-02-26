package bestseller.com.TaskMangement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bestseller.com.TaskMangement.model.ForgotPasswordOtp;
import bestseller.com.TaskMangement.model.User;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPasswordOtp, Integer> {
    Optional<ForgotPasswordOtp> findByUser(User user);
}
