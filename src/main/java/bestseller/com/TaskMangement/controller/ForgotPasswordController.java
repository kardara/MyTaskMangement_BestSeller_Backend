package bestseller.com.TaskMangement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bestseller.com.TaskMangement.service.ForgotPasswordService;

@RestController
@RequestMapping("/forgot-password")
@CrossOrigin(origins = "http://localhost:5173")
public class ForgotPasswordController {

    @Autowired
    private  ForgotPasswordService forgotPasswordService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        String result = forgotPasswordService.sendOtp(email);
        if (result.equals("No account found with this email")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/verify-otp/{otp}")
    public ResponseEntity<?> verifyOtp(@PathVariable Integer otp, @RequestParam String email) {
        String result = forgotPasswordService.verifyOtp(otp, email);
        if (result.equals("OTP verified successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,@RequestParam String newPassword) {
        String result = forgotPasswordService.resetPassword(email, newPassword);
        if (result.equals("Password reset successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
