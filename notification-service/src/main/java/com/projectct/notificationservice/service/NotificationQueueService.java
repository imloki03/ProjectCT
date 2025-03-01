package com.projectct.notificationservice.service;

import com.projectct.notificationservice.DTO.Notification.response.NotificationResponse;

import java.util.List;

public interface NotificationQueueService {
    List<NotificationResponse> getAllNotificationOfUser(Long userId);
}
