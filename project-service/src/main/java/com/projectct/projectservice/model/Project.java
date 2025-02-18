package com.projectct.projectservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;
    private String name;
    private String description;
    private String avatarURL;
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
    private Backlog backlog;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Phase> phaseList;

    private Long chatboxId;
    private Long notificationQueueId;
    private Long storageId;

    @ElementCollection
    @CollectionTable(name = "project_collab", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "collab_id")
    private List<Long> collaboratorIdList;
}
