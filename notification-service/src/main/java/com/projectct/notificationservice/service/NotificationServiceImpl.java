package com.projectct.notificationservice.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;
import com.projectct.notificationservice.DTO.FCMToken.request.FCMTokenRequest;
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
    public void sendMessageToTopic(String topic) {
        Notification notification = Notification.builder()
                .setTitle("Test Notification")
                .setBody("This is a test message for topic: " + topic)
                .build();

        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .build();

        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully sent test message to topic: {}", response);
        } catch (Exception e) {
            log.info("Failed to send test message to topic: {} ", e.getMessage());
        }
    }

    @Override
    public void subscribeToTopics(SubscriptionRequest request) {
        String token = request.getToken();
        List<String> topics = request.getTopics();
        try {
            for (String topic : topics) {
                TopicManagementResponse response = firebaseMessaging.subscribeToTopic(List.of(token), topic);
                log.info("Subscribed to topic: {} with {} successes and {} failures",
                        topic, response.getSuccessCount(), response.getFailureCount());
            }
            log.info("Successfully subscribed to topics: {}", topics);
        } catch (Exception e) {
            log.error("Failed to subscribe to topics");
        }
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
}
