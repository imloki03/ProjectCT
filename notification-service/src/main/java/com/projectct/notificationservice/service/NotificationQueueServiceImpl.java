package com.projectct.notificationservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectct.notificationservice.DTO.Notification.response.NotificationResponse;
import com.projectct.notificationservice.model.Notification;
import com.projectct.notificationservice.repository.NotificationQueueRepository;
import com.projectct.notificationservice.repository.NotificationRepository;
import com.projectct.notificationservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationQueueServiceImpl implements NotificationQueueService {
    final NotificationRepository notificationRepository;

    @Override
    public Page<NotificationResponse> getAllReadNotificationOfUser(Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByNotificationQueue_UserIdAndIsReadTrue(userId, pageable);
        long totalElements = notificationRepository.countByNotificationQueue_UserIdAndIsReadTrue(userId);

        List<NotificationResponse> responses = notifications.stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .isRead(notification.isRead())
                        .title(buildStringBack(notification.getTitleKey(), null))
                        .content(buildStringBack(notification.getMessageKey(), notification.getArgsJson()))
                        .referenceLink(notification.getReferenceLink())
                        .sentTime(notification.getSentTime())
                        .relevantAvatarUrl(notification.getRelevantAvatarUrl())
                        .relevantName(notification.getRelevantName())
                        .build())
                .toList();

        return new PageImpl<>(responses, pageable, totalElements);
    }

    @Override
    public Page<NotificationResponse> getAllUnReadNotificationOfUser(Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByNotificationQueue_UserIdAndIsReadFalse(userId, pageable);
        long totalElements = notificationRepository.countByNotificationQueue_UserIdAndIsReadFalse(userId);

        List<NotificationResponse> responses = notifications.stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .isRead(notification.isRead())
                        .title(buildStringBack(notification.getTitleKey(), null))
                        .content(buildStringBack(notification.getMessageKey(), notification.getArgsJson()))
                        .referenceLink(notification.getReferenceLink())
                        .sentTime(notification.getSentTime())
                        .relevantAvatarUrl(notification.getRelevantAvatarUrl())
                        .relevantName(notification.getRelevantName())
                        .build())
                .toList();

        return new PageImpl<>(responses, pageable, totalElements);
    }

    private String buildStringBack(String messageKey, String argsJson) {
        try {
            if (argsJson == null || argsJson.trim().isEmpty()) {
                return MessageUtil.getMessage(messageKey);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> argsList = objectMapper.readValue(argsJson, new TypeReference<List<Object>>() {});

            Object[] highlightedArgs = argsList.stream()
                    .map(arg -> "<b>" + arg + "</b>")
                    .toArray();

            return MessageUtil.getMessage(messageKey, highlightedArgs);
        } catch (Exception e) {
            log.error("Failed to build message for key: {}, argsJson: {}", messageKey, argsJson, e);
            return messageKey;
        }
    }

}
