package com.projectct.collabservice.DTO.Notification.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DirectNotificationRequest {
    Long recipient;
    List<String> tokens;
    Long projectId;
    String projectName;
    String projectAvatarURL;
    String senderUsername;
    String type;
}
