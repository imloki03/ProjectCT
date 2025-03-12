package com.projectct.projectservice.model;

import com.projectct.projectservice.enums.Priority;
import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.enums.TaskType;
import com.projectct.projectservice.model.listener.TaskListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(TaskListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @OneToMany(mappedBy = "parentTask", orphanRemoval = true)
    private List<Task> subTask;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    private Long assigneeId;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ElementCollection
    @CollectionTable(name = "task_media", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "media_id")
    private List<Long> mediaIdList;

    @ElementCollection
    @CollectionTable(name = "task_proof", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "proof_id")
    private List<Long> proofList;

    @ManyToOne
    private Backlog backlog;

    @ManyToOne
    private Phase phase;

    public void addSubTask(Task subTask) {
        if (this.subTask == null)
            this.subTask = new ArrayList<>();
        this.subTask.add(subTask);
    }
}
