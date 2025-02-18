package com.projectct.authservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String username;
    private String name;
    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String email;
    private String password;
    private String gender;
    private String avatarURL;

    @ManyToMany
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tagList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private UserStatus status;

    @ElementCollection
    @CollectionTable(name = "user_project", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "project_id")
    private List<Long> projectIdList;

    @ElementCollection
    @CollectionTable(name = "user_task", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "task_id")
    private List<Long> taskIdList;

    @ElementCollection
    @CollectionTable(name = "user_collab", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "collab_id")
    private List<Long> collabWithIdList;

    private Long notificationQueueId;
}