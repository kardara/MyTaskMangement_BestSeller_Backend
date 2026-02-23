package bestseller.com.TaskMangement.dto;

import bestseller.com.TaskMangement.model.ETaskStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private ETaskStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime reminderTime;
    private Boolean reminderSent;
    private Long userId;
}
