    // ...existing code...
package bestseller.com.TaskMangement.service;

import bestseller.com.TaskMangement.model.Task;
import bestseller.com.TaskMangement.model.User;
import bestseller.com.TaskMangement.repository.TaskRepository;
import bestseller.com.TaskMangement.repository.UserRepository;
import bestseller.com.TaskMangement.dto.TaskRequest;
import bestseller.com.TaskMangement.dto.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
   
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    public List<TaskResponse> getTasksForUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) return new ArrayList<>();
        return taskRepository.findByUser(user.get()).stream()
                .map(task->toTaskResponse(task))
                .toList();
    }

    @CacheEvict(value = "tasks", key = "#result.id")
    public TaskResponse saveTask(TaskRequest taskRequest, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) return null;
        Task task = toTaskEntity(taskRequest);
        task.setUser(user.get());
        Task saved = taskRepository.save(task);
        return toTaskResponse(saved);
    }

    @Cacheable(value = "tasks", key = "#id")
    public TaskResponse getTaskById(Long id, String email) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getEmail().equals(email)) {
            return toTaskResponse(task.get());
        }
        return null;
    }

    @CacheEvict(value = "tasks", key = "#id")
    public String deleteTask(Long id, String email) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getEmail().equals(email)) {
            taskRepository.deleteById(id);
            return "Task deleted successfully";
        } else if (task.isPresent()) {
            return "Not allowed";
        } else {
            return "Task not found";
        }
    }

    @CachePut(value = "tasks", key = "#id")
    public String updateTask(Long id, TaskRequest taskRequest, String email) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent() && taskOpt.get().getUser().getEmail().equals(email)) {
            Task task = taskOpt.get();
            task.setTitle(taskRequest.getTitle());
            task.setDescription(taskRequest.getDescription());
            task.setStatus(taskRequest.getStatus());
            task.setDueDate(taskRequest.getDueDate());
            task.setReminderTime(taskRequest.getReminderTime());
            taskRepository.save(task);
            return "Task updated successfully";
        } else if (taskOpt.isPresent()) {
            return "Not allowed";
        } else {
            return "Task not found";
        }
    }

    private TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .reminderTime(task.getReminderTime())
                .reminderSent(task.getReminderSent())
                .userId(task.getUser() != null ? task.getUser().getUserId() : null)
                .build();
    }

    private Task toTaskEntity(TaskRequest dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .dueDate(dto.getDueDate())
                .reminderTime(dto.getReminderTime())
                .build();
    }
}
