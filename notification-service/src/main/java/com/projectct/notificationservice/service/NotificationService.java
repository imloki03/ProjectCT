package com.projectct.notificationservice.service;

import com.projectct.notificationservice.DTO.Subcribe.request.SubscriptionRequest;

public interface NotificationService {
    void sendMessageToTopic(String topic);

    void subscribeToTopics(SubscriptionRequest request);

    void readNotification(Long notificationId);
}
