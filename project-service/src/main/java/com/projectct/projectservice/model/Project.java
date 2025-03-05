package com.projectct.projectservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    private String ownerUsername;
    private String name;
    private String description;
    private String avatarURL;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
    private Backlog backlog;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Phase> phaseList;
}
