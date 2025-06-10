package com.projectct.notificationservice.service;

import com.projectct.notificationservice.DTO.Subcribe.request.SubscriptionRequest;

public interface NotificationService {
    String subscribeToTopics(SubscriptionRequest request);

    void readNotification(Long notificationId);

    String unsubscribeToTopic(SubscriptionRequest request);

    String sendTestMessageToTopic(String topic, String title, String body);
}
