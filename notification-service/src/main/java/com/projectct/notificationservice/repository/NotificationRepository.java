package com.projectct.notificationservice.repository;

import com.projectct.notificationservice.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByNotificationQueue_UserIdAndIsReadTrue(Long userId, Pageable pageable);
    Page<Notification> findByNotificationQueue_UserIdAndIsReadFalse(Long userId, Pageable pageable);

    long countByNotificationQueue_UserIdAndIsReadTrue(Long userId);
    long countByNotificationQueue_UserIdAndIsReadFalse(Long userId);
}