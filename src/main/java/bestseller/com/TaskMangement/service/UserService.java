package bestseller.com.TaskMangement.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bestseller.com.TaskMangement.dto.LoginRequest;
import bestseller.com.TaskMangement.dto.RegisterRequest;
import bestseller.com.TaskMangement.dto.UserResponse;
import bestseller.com.TaskMangement.model.ERole;
import bestseller.com.TaskMangement.model.User;
import bestseller.com.TaskMangement.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public String saveUser(RegisterRequest user) {
        boolean exists = userRepository.existsByEmail(user.getEmail());
        if (exists) {
            return "User with this email already exists";
        }
        User newUser = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(ERole.USER)
                .build();
        userRepository.save(newUser);
        return "User saved successfully";
    }
    
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponse> response = users.map(user -> new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole()));
        return response;
    }

    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

    if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
    return null;
    }

    @CacheEvict(value = "users", key = "#id")
    public String updateUser(Long id, RegisterRequest userRequest) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setName(userRequest.getName());
            existingUser.setEmail(userRequest.getEmail());
            existingUser.setPassword(userRequest.getPassword());
            userRepository.save(existingUser);
            return "User updated successfully";
        } else {
            return "User not found";
        }
    }

    @CacheEvict(value = "users", key = "#id")
    public String deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }

    public String loginUser(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        if(!user.isPresent()){
            return "Invalid email or password";
        }
        return "Login successful";
    }
}
