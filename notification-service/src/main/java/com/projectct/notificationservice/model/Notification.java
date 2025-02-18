package com.projectct.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipientId;
    private String content;
    private LocalDateTime sentTime;
    private boolean isRead;
    private String referenceLink;

    @ManyToOne
    @JoinColumn(name = "notiqueue_id")
    private NotificationQueue notificationQueue;
}
