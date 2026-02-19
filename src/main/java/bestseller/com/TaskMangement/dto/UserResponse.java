package bestseller.com.TaskMangement.dto;

import bestseller.com.TaskMangement.model.ERole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String name;
    private String email;
    private ERole role;
}

