package com.projectct.notificationservice.DTO.Notification.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    Long id;
    String title;
    String content;
    LocalDateTime sentTime;
    Boolean isRead;
    String referenceLink;
    String relevantName;
    String relevantAvatarUrl;
}
