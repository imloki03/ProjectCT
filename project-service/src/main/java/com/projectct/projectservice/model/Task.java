package com.projectct.projectservice.model;

import com.projectct.projectservice.enums.Priority;
import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.enums.TaskType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @OneToMany(mappedBy = "parentTask", orphanRemoval = true)
    private List<Task> subTask;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    private Long assigneeId;

    @Enumerated(EnumType.STRING)
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

    public List<Long> getSubtaskIdList(){
        List<Long> subtaskId = new ArrayList<>();
        for (Task task : subTask) {
            subtaskId.add(task.getId());
        }
        return subtaskId;
    }

    public void addSubTask(Task subTask) {
        if (this.subTask == null)
            this.subTask = new ArrayList<>();
        this.subTask.add(subTask);
    }
}
