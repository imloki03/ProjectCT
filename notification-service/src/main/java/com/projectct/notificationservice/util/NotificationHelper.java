package com.projectct.notificationservice.util;

import com.google.firebase.messaging.Notification;
import com.netflix.discovery.converters.Auto;
import com.projectct.notificationservice.DTO.Notification.request.DirectNotificationRequest;
import com.projectct.notificationservice.DTO.Notification.request.TopicNotificationRequest;
import com.projectct.notificationservice.constant.NotificationMessageKey;
import com.projectct.notificationservice.constant.NotificationRefLink;
import com.projectct.notificationservice.constant.NotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class NotificationHelper {
    private final Map<String, Function<DirectNotificationRequest, Notification>> directBuilders = new HashMap<>();
    private final Map<String, Function<TopicNotificationRequest, Notification>> topicBuilders = new HashMap<>();

    private static final ThreadLocal<String> lastTitleKey = new ThreadLocal<>();
    private static final ThreadLocal<String> lastMessageKey = new ThreadLocal<>();
    private static final ThreadLocal<Object[]> lastArgs = new ThreadLocal<>();
    private static final ThreadLocal<String> lastRefLink = new ThreadLocal<>();

    public static String getLastMessageKey() {
        return lastMessageKey.get();
    }

    public static Object[] getLastArgs() {
        return lastArgs.get();
    }

    public static String getLastTitleKey() {
        return lastTitleKey.get();
    }

    public static String getLastRefLink() { return lastRefLink.get(); }


    public NotificationHelper() {
        directBuilders.put(NotificationType.ADD_COLLAB_TO_PROJECT, this::buildInviteNotification);

        topicBuilders.put(NotificationType.ADD_COLLAB_TO_PROJECT, this::buildInviteTopicNotification);
    }

    public Notification buildDirectNotification(DirectNotificationRequest request) {
        Function<DirectNotificationRequest, Notification> builder = directBuilders.get(request.getType());
        if (builder == null) {
            throw new IllegalArgumentException("Unsupported direct notification type: " + request.getType());
        }
        return builder.apply(request);
    }

    public Notification buildTopicNotification(TopicNotificationRequest request) {
        Function<TopicNotificationRequest, Notification> builder = topicBuilders.get(request.getType());
        if (builder == null) {
            throw new IllegalArgumentException("Unsupported topic notification type: " + request.getType());
        }
        return builder.apply(request);
    }

    // Direct
    private Notification buildInviteNotification(DirectNotificationRequest request) {
        String titleKey = NotificationMessageKey.PROJECT_NOTIFICATION_TITLE;
        String key = NotificationMessageKey.ADD_COLLAB_TO_PROJECT_DIRECT;
        String refLinkKey = NotificationRefLink.COLLAB_LINK;
        Object[] args = new Object[]{request.getProjectName()};

        lastTitleKey.set(titleKey);
        lastMessageKey.set(key);
        lastArgs.set(args);
        lastRefLink.set(refLinkKey);

        return Notification.builder()
                .setTitle(MessageUtil.getMessage(titleKey, args))
                .setBody(MessageUtil.getMessage(key, args))
                .build();
    }

    // Topic
    private Notification buildInviteTopicNotification(TopicNotificationRequest request) {
        String titleKey = NotificationMessageKey.PROJECT_NOTIFICATION_TITLE;
        String key = NotificationMessageKey.ADD_COLLAB_TO_PROJECT_TOPIC;
        Object[] args = new Object[]{request.getRelevantName(), request.getProjectName()};
        String refLinkKey = NotificationRefLink.COLLAB_LINK;

        lastTitleKey.set(titleKey);
        lastMessageKey.set(key);
        lastArgs.set(args);
        lastRefLink.set(refLinkKey);

        return Notification.builder()
                .setTitle(MessageUtil.getMessage(titleKey, args))
                .setBody(MessageUtil.getMessage(key, args))
                .build();
    }
}
