package bestseller.com.TaskMangement.dto;

import bestseller.com.TaskMangement.model.ETaskStatus;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    private String title;
    private String description;
    private ETaskStatus status;
}
