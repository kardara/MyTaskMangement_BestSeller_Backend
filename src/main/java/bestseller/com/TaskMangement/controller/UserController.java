package bestseller.com.TaskMangement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bestseller.com.TaskMangement.dto.AuthResponse;
import bestseller.com.TaskMangement.dto.ErrorDto;
import bestseller.com.TaskMangement.dto.LoginRequest;
import bestseller.com.TaskMangement.dto.RegisterRequest;
import bestseller.com.TaskMangement.dto.UserResponse;
import bestseller.com.TaskMangement.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody RegisterRequest user) {
        String result = userService.saveUser(user);
        if (result.equals("User with this email already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body( new ErrorDto(result));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        Page<UserResponse> users = userService.getAllUsers(pageable);
        if (users == null || users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ErrorDto("No users found"));
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("User not found"));
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody RegisterRequest userRequest) {
        String result = userService.updateUser(id, userRequest);
        if (result.equals("User not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(result));
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        String result = userService.deleteUser(id);
        if (result.equals("User not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(result));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = userService.loginUser(loginRequest);
        if (authResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto("Invalid email or password"));
        }
        return ResponseEntity.ok(authResponse);
    }
}
