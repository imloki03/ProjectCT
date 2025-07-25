package com.projectct.notificationservice.DTO.Notification.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TopicNotificationRequest {
    Long projectId;
    String projectName;
    String projectAvatarURL;
    String relevantName;
    String type;
}
