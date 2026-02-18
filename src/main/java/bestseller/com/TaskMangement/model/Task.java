package bestseller.com.TaskMangement.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class Task {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private ETaskStatus status;

    private LocalDateTime dueDate;

    private LocalDateTime reminderTime;

    private Boolean reminderSent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
