package com.projectct.notificationservice.service.KafkaConsumer;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.projectct.notificationservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.notificationservice.DTO.Notification.request.DirectNotificationRequest;
import com.projectct.notificationservice.DTO.Notification.request.TopicNotificationRequest;
import com.projectct.notificationservice.DTO.Project.response.ProjectResponse;
import com.projectct.notificationservice.constant.KafkaTopic;
import com.projectct.notificationservice.model.NotificationQueue;
import com.projectct.notificationservice.repository.NotificationQueueRepository;
import com.projectct.notificationservice.repository.NotificationRepository;
import com.projectct.notificationservice.repository.httpclient.CollabClient;
import com.projectct.notificationservice.repository.httpclient.ProjectClient;
import com.projectct.notificationservice.util.NotificationHelper;
import com.projectct.notificationservice.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    final NotificationRepository notificationRepository;
    final NotificationQueueRepository notificationQueueRepository;
    final FirebaseMessaging firebaseMessaging;
    final ObjectMapperUtil objectMapperUtil;
    final CollabClient collabClient;
    final NotificationHelper notificationHelper;
    final ProjectClient projectClient;

    @KafkaListener(topics = KafkaTopic.USER_DIRECT_NOTIFICATION)
    public void sendNotificationToUser(String s) {
        DirectNotificationRequest request = objectMapperUtil.deserializeFromJson(s, DirectNotificationRequest.class);
        try {
            ProjectResponse projectData = enrichProjectData(request);
            String avatarUrl = getAvatarUrl(request);
            Notification notification = notificationHelper.buildDirectNotification(request);

            Long notificationId = saveNotification(request.getRecipient(), request.getProjectName(), avatarUrl, projectData);

            for (String token : request.getTokens()) {
                Message message = Message.builder()
                        .setToken(token)
                        .setNotification(notification)
                        .putData("id", String.valueOf(notificationId))
                        .putData("sentTime", LocalDateTime.now().toString())
                        .putData("isRead", "false")
                        .putData("referenceLink", buildReferenceLink(projectData))
                        .putData("relevantName", request.getProjectName())
                        .putData("relevantAvatarUrl", avatarUrl)
                        .build();

                log.info("Sent direct notification to [{}]: {}", token, firebaseMessaging.send(message));
            }

        } catch (Exception e) {
            log.error("Failed to send direct notification: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaTopic.TOPIC_NOTIFICATION)
    public void sendNotificationToTopic(String s) {
        TopicNotificationRequest request = objectMapperUtil.deserializeFromJson(s, TopicNotificationRequest.class);
        try {
            ProjectResponse projectData = enrichProjectData(request);
            String avatarUrl = getAvatarUrl(request);
            Notification notification = notificationHelper.buildTopicNotification(request);

            for (CollabResponse collab : collabClient.getAllCollabFromProject(request.getProjectId()).getData()) {
                Long notificationId = saveNotification(collab.getUserId(), request.getProjectName(), avatarUrl, projectData);

                if (collab.getUser() != null && collab.getUser().getFcmTokens() != null) {
                    for (String token : collab.getUser().getFcmTokens()) {
                        if (token == null || token.isBlank()) {
                            log.warn("Skipping null or blank FCM token for user [{}]", collab.getUserId());
                            continue;
                        }

                        Message message = Message.builder()
                                .setToken(token)
                                .setNotification(notification)
                                .putData("id", String.valueOf(notificationId))
                                .putData("sentTime", LocalDateTime.now().toString())
                                .putData("isRead", "false")
                                .putData("referenceLink", buildReferenceLink(projectData))
                                .putData("relevantName", request.getProjectName())
                                .putData("relevantAvatarUrl", avatarUrl)
                                .build();

                        log.info("Sent topic notification to [{}] - token [{}]: {}", collab.getUserId(), token, firebaseMessaging.send(message));
                    }
                } else {
                    log.warn("User or FCM token list is null for collaborator with ID [{}]", collab.getUserId());
                }
            }
        } catch (Exception e) {
            log.error("Failed to send topic notification: {}", e.getMessage());
        }
    }

    private ProjectResponse enrichProjectData(Object request) {
        try {
            Long projectId = request instanceof DirectNotificationRequest ?
                    ((DirectNotificationRequest) request).getProjectId() :
                    ((TopicNotificationRequest) request).getProjectId();

            ProjectResponse projectData = projectClient.getProject(projectId).getData();
            if (projectData != null) {
                if (request instanceof DirectNotificationRequest direct) {
                    if (direct.getProjectName() == null) direct.setProjectName(projectData.getName());
                    if (direct.getProjectAvatarURL() == null) direct.setProjectAvatarURL(projectData.getAvatarURL());
                } else if (request instanceof TopicNotificationRequest topic) {
                    if (topic.getProjectName() == null) topic.setProjectName(projectData.getName());
                    if (topic.getProjectAvatarURL() == null) topic.setProjectAvatarURL(projectData.getAvatarURL());
                }
            }
            return projectData;
        } catch (Exception e) {
            log.warn("Project enrichment failed: {}", e.getMessage());
            return null;
        }
    }

    private String getAvatarUrl(Object request) {
        if (request instanceof DirectNotificationRequest direct) {
            return direct.getProjectAvatarURL() != null ? direct.getProjectAvatarURL() : "";
        } else if (request instanceof TopicNotificationRequest topic) {
            return topic.getProjectAvatarURL() != null ? topic.getProjectAvatarURL() : "";
        }
        return "";
    }

    private String buildReferenceLink(ProjectResponse projectData) {
        if (projectData == null) return "";
        return MessageFormat.format(
                NotificationHelper.getLastRefLink(),
                projectData.getOwnerUsername(),
                projectData.getName().replace(" ", "_")
        );
    }

    private Long saveNotification(Long userId, String projectName, String avatarUrl, ProjectResponse projectData) {
        NotificationQueue queue = notificationQueueRepository.findByUserId(userId);
        com.projectct.notificationservice.model.Notification saved = notificationRepository.save(
                com.projectct.notificationservice.model.Notification.builder()
                        .recipientId(userId)
                        .sentTime(LocalDateTime.now())
                        .titleKey(NotificationHelper.getLastTitleKey())
                        .messageKey(NotificationHelper.getLastMessageKey())
                        .argsJson(objectMapperUtil.toJson(NotificationHelper.getLastArgs()))
                        .notificationQueue(queue)
                        .relevantName(projectName)
                        .relevantAvatarUrl(avatarUrl)
                        .referenceLink(buildReferenceLink(projectData))
                        .build()
        );
        return saved.getId();
    }
}
