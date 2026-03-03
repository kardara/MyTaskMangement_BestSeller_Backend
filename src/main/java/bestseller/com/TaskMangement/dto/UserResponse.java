package bestseller.com.TaskMangement.dto;

import bestseller.com.TaskMangement.model.ERole;

public record UserResponse(Long userId, String name, String email, ERole role, boolean isDeleted) {
}

