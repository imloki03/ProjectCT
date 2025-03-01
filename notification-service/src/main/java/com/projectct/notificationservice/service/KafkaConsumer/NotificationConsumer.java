package com.projectct.notificationservice.service.KafkaConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.projectct.notificationservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.notificationservice.DTO.FCMToken.request.FCMTokenRequest;
import com.projectct.notificationservice.DTO.Notification.request.DirectNotificationRequest;
import com.projectct.notificationservice.DTO.Notification.request.TopicNotificationRequest;
import com.projectct.notificationservice.constant.KafkaTopic;
import com.projectct.notificationservice.constant.NotificationType;
import com.projectct.notificationservice.model.NotificationQueue;
import com.projectct.notificationservice.repository.NotificationQueueRepository;
import com.projectct.notificationservice.repository.NotificationRepository;
import com.projectct.notificationservice.repository.httpclient.CollabClient;
import com.projectct.notificationservice.service.NotificationService;
import com.projectct.notificationservice.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    final NotificationRepository notificationRepository;
    final NotificationQueueRepository notificationQueueRepository;
    final FirebaseMessaging firebaseMessaging;
    final ObjectMapperUtil objectMapperUtil;
    final CollabClient collabClient;

    @KafkaListener(topics = KafkaTopic.USER_DIRECT_NOTIFICATION)
    public void sendNotificationToUser(String s) {
        DirectNotificationRequest request = objectMapperUtil.deserializeFromJson(s, DirectNotificationRequest.class);
        Notification notification ;
        Message message = null;
        String content = "";
        if (request.getType().equals(NotificationType.INVITE_NOTIFICATION))
        {
            content = "Invitation to join project " + request.getProjectName() + " from " + request.getSenderUsername();
            notification = Notification.builder()
                    .setTitle("Invite Notification")
                    .setBody(content)
                    .build();

            message = Message.builder()
                    .setToken(request.getToken())
                    .setNotification(notification)
                    .build();
        }
        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully sent message to user: {}", response);
            notificationRepository.save(com.projectct.notificationservice.model.Notification.builder()
                                        .recipientId(request.getRecipient())
                                        .content(content)
                                        .sentTime(LocalDateTime.now())
                                        .notificationQueue(notificationQueueRepository.findByUserId(request.getRecipient()))
                                        .build());
        } catch (Exception e) {
            log.error("Failed to send message to user: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaTopic.TOPIC_NOTIFICATION)
    public void sendNotificationToTopic(String s) {
        TopicNotificationRequest request = objectMapperUtil.deserializeFromJson(s, TopicNotificationRequest.class);
//        Notification notification ;
//        Message message = null;
        String content = "Test topic";
        Notification notification = Notification.builder()
                .setTitle("Test Notification")
                .setBody("This is a test message for topic: project" + request.getProjectId())
                .build();

        Message message = Message.builder()
                .setTopic("project"+request.getProjectId())
                .setNotification(notification)
                .build();
        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully sent message to topic: {}", response);
            List<CollabResponse> collabResponses = collabClient.getAllCollabFromProject(request.getProjectId()).getData();
            for(CollabResponse r : collabResponses)
            {
                notificationRepository.save(com.projectct.notificationservice.model.Notification.builder()
                        .recipientId(r.getUserId())
                        .content(content)
                        .sentTime(LocalDateTime.now())
                        .notificationQueue(notificationQueueRepository.findByUserId(r.getUserId()))
                        .build());
            }
        } catch (Exception e) {
            log.error("Failed to send message to topic: {}", e.getMessage());
        }
    }
}
