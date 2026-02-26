package bestseller.com.TaskMangement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bestseller.com.TaskMangement.dto.ChangePassword;
import bestseller.com.TaskMangement.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/forgot-password")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    // Step 1: Send OTP to email (email must exist in DB)
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        String result = forgotPasswordService.sendOtp(email);
        if (result.equals("No account found with this email")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    // Step 2: Verify OTP
    @PostMapping("/verify-otp/{otp}")
    public ResponseEntity<?> verifyOtp(@PathVariable Integer otp, @RequestParam String email) {
        String result = forgotPasswordService.verifyOtp(otp, email);
        if (result.equals("OTP verified successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // Step 3: Reset password after OTP verification
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestBody ChangePassword changePassword) {
        String result = forgotPasswordService.resetPassword(email, changePassword);
        if (result.equals("Password reset successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
