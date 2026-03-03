
package bestseller.com.TaskMangement.controller;

import bestseller.com.TaskMangement.dto.TaskRequest;
import bestseller.com.TaskMangement.dto.TaskResponse;
import bestseller.com.TaskMangement.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest, Authentication authentication) {
        String email = authentication.getName();
        String result = taskService.updateTask(id, taskRequest, email);
        if (result.equals("Task updated successfully")) {
            return ResponseEntity.ok(result);
        } else if (result.equals("Not allowed")) {
            return ResponseEntity.status(403).body(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getMyTasks(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(taskService.getTasksForUser(email));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(taskService.saveTask(taskRequest, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        String result = taskService.deleteTask(id, email);
        if (result.equals("Task deleted successfully")) {
            return ResponseEntity.ok(result);
        } else if (result.equals("Not allowed")) {
            return ResponseEntity.status(403).body(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }
}
