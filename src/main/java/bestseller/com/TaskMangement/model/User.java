package bestseller.com.TaskMangement.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToMany
    private List<Task> tasks = new ArrayList<>();
}
