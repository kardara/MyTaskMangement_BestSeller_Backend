package bestseller.com.TaskMangement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bestseller.com.TaskMangement.dto.LoginRequest;
import bestseller.com.TaskMangement.dto.RegisterRequest;
import bestseller.com.TaskMangement.dto.UserResponse;
import bestseller.com.TaskMangement.model.ERole;
import bestseller.com.TaskMangement.model.User;
import bestseller.com.TaskMangement.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;


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
    
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole()))
                .toList();
    }

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
