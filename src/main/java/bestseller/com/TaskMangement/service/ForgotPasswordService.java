package bestseller.com.TaskMangement.service;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bestseller.com.TaskMangement.dto.ChangePassword;
import bestseller.com.TaskMangement.dto.MailBody;
import bestseller.com.TaskMangement.model.ForgotPasswordOtp;
import bestseller.com.TaskMangement.model.User;
import bestseller.com.TaskMangement.repository.ForgotPasswordRepo;
import bestseller.com.TaskMangement.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final ForgotPasswordRepo forgotPasswordRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // Step 1: Send OTP — email must exist in DB
    public String sendOtp(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "No account found with this email";
        }
        User user = userOpt.get();

        int otp = new Random().nextInt(100000, 999999);
        // OTP expires in 10 minutes
        Date expirationDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);

        // Delete any existing OTP for this user before creating a new one
        forgotPasswordRepo.findByUser(user).ifPresent(forgotPasswordRepo::delete);

        ForgotPasswordOtp forgotPasswordOtp = new ForgotPasswordOtp(null, otp, expirationDate, user);
        forgotPasswordRepo.save(forgotPasswordOtp);

        MailBody mailBody = new MailBody(
                email,
                "Password Reset OTP - TaskManagement",
                "Your OTP for password reset is: " + otp + "\n\nThis OTP will expire in 10 minutes."
        );
        emailService.sendSimpleMessage(mailBody);

        return "OTP sent successfully to " + email;
    }

    // Step 2: Verify OTP
    public String verifyOtp(Integer otp, String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "No account found with this email";
        }
        User user = userOpt.get();

        Optional<ForgotPasswordOtp> otpOpt = forgotPasswordRepo.findByUser(user);
        if (otpOpt.isEmpty()) {
            return "OTP not found. Please request a new OTP";
        }

        ForgotPasswordOtp forgotPasswordOtp = otpOpt.get();

        // Check expiration
        if (forgotPasswordOtp.getExpirationDate().before(Date.from(Instant.now()))) {
            forgotPasswordRepo.delete(forgotPasswordOtp);
            return "OTP has expired. Please request a new one";
        }

        // Check OTP match
        if (!forgotPasswordOtp.getOtp().equals(otp)) {
            return "Invalid OTP";
        }

        return "OTP verified successfully";
    }

    // Step 3: Reset password (only after OTP is verified)
    public String resetPassword(String email, ChangePassword changePassword) {
        if (!changePassword.getPassword().equals(changePassword.getRepeatPassword())) {
            return "Passwords do not match";
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "No account found with this email";
        }
        User user = userOpt.get();

        // Delete OTP record after successful reset
        forgotPasswordRepo.findByUser(user).ifPresent(forgotPasswordRepo::delete);

        user.setPassword(passwordEncoder.encode(changePassword.getPassword()));
        userRepository.save(user);

        return "Password reset successfully";
    }
}
