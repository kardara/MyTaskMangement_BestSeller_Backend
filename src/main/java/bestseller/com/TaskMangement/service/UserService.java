package bestseller.com.TaskMangement.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import bestseller.com.TaskMangement.security.JwtUtil;
import bestseller.com.TaskMangement.dto.AuthResponse;
import bestseller.com.TaskMangement.exceptions.AccountBlockedException;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public String saveUser(RegisterRequest user) {
        boolean exists = userRepository.existsByEmail(user.getEmail());
        if (exists) {
            return "User with this email already exists";
        }
        User newUser = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(ERole.USER)
                .isDeleted(false)
                .build();
        userRepository.save(newUser);
        return "User saved successfully";
    }

        public UserResponse mapToUserResponse(User user) {
            return new UserResponse(
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.isDeleted()
            );
        }
    
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponse> response = users.map(user -> new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole(), user.isDeleted()));
        return response;
    }

    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isDeleted()) {
                return null;
            }
            return new UserResponse(
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.isDeleted()
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
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userRepository.save(existingUser);
            return "User updated successfully";
        } else {
            return "User not found";
        }
    }

    @CacheEvict(value = "users", key = "#id")
    public String blockUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isDeleted()) return "User is already blocked";
            user.setDeleted(true);
            userRepository.save(user);
            return "User blocked successfully";
        } else {
            return "User not found";
        }
    }

    @CacheEvict(value = "users", key = "#id")
    public String unblockUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.isDeleted()) return "User is not blocked";
            user.setDeleted(false);
            userRepository.save(user);
            return "User unblocked successfully";
        } else {
            return "User not found";
        }
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            return null;
        }
        User user = userOpt.get();
        if (user.isDeleted()) {
            throw new AccountBlockedException("This account has been blocked. Please contact the administrator.");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return null;
        }
        String token = jwtUtil.generateToken(user.getUsername());
        UserResponse userResponse = mapToUserResponse(user);
        return new AuthResponse(token, userResponse);
    }
}
