package bestseller.com.TaskMangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bestseller.com.TaskMangement.model.ForgotPasswordOtp;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPasswordOtp, Integer> {
    
}
