package bestseller.com.TaskMangement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
