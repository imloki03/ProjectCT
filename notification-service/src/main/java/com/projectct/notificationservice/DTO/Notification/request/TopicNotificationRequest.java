package com.projectct.notificationservice.DTO.Notification.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicNotificationRequest {
    Long projectId;
    String projectName;
    String type;
}
