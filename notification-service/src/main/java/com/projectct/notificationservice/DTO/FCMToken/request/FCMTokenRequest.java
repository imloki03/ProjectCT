package com.projectct.notificationservice.DTO.FCMToken.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FCMTokenRequest {
    String token;
    Long userId;
}
