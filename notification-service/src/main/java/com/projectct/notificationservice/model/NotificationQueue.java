package com.projectct.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "notificationQueue", cascade = CascadeType.REMOVE)
    private List<Notification> notificationList;

    private Long projectId;
}
