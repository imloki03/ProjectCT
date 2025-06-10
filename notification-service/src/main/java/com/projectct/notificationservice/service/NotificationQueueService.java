package com.projectct.notificationservice.service;

import com.projectct.notificationservice.DTO.Notification.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationQueueService {
    Page<NotificationResponse> getAllReadNotificationOfUser(Long userId, Pageable pageable);

    Page<NotificationResponse> getAllUnReadNotificationOfUser(Long userId, Pageable pageable);
}
