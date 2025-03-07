package com.projectct.projectservice.model;

import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.model.listener.PhaseListener;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(PhaseListener.class)
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "phase")
    private List<Task> taskList;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @PreRemove
    private void preRemove() {
        taskList.forEach(task -> task.setPhase(null));
    }
}
