package bestseller.com.TaskMangement.dto;

import bestseller.com.TaskMangement.model.ETaskStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private ETaskStatus status;
    private Long userId;
}
