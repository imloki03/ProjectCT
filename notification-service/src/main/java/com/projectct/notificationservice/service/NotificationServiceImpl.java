package com.projectct.notificationservice.service;

import com.google.firebase.messaging.*;
import com.projectct.notificationservice.DTO.Subcribe.request.SubscriptionRequest;
import com.projectct.notificationservice.constant.MessageKey;
import com.projectct.notificationservice.exception.AppException;
import com.projectct.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    final NotificationRepository notificationRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public String subscribeToTopics(SubscriptionRequest request) {
        String token = request.getToken();
        List<String> topics = request.getTopics();
        StringBuilder result = new StringBuilder();

        try {
            for (String topic : topics) {
                TopicManagementResponse response = FirebaseMessaging.getInstance()
                        .subscribeToTopic(List.of(token), topic);

                result.append(String.format("Subscribed to topic: %s with %d successes and %d failures%n",
                        topic, response.getSuccessCount(), response.getFailureCount()));
            }
        } catch (Exception e) {
            log.error("Failed to subscribe to topics", e);
            return "Subscription failed due to an error.";
        }
        return result.toString();
    }

    @Override
    public void readNotification(Long notificationId) {
        com.projectct.notificationservice.model.Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.NOTIFICATION_NOT_FOUND);
        }
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public String unsubscribeToTopic(SubscriptionRequest request) {
        String token = request.getToken();
        for (String topic : request.getTopics()) {
            try {
                firebaseMessaging.unsubscribeFromTopic(List.of(token), topic);
            } catch (FirebaseMessagingException e) {
                return "Subscription failed due to an error.";
            }
        }
        return "Successfully unsubscribed from topic: " + request.getTopics().getFirst();
    }
}
