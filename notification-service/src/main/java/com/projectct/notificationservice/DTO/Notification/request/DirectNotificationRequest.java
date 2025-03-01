package com.projectct.notificationservice.DTO.Notification.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectNotificationRequest {
    Long recipient;
    String token;
    Long projectId;
    String projectName;
    String senderUsername;
    String type;
}
