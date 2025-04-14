package com.projectct.authservice.DTO.User.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOAuthUserRequest {
    String username;
    String name;
    String email;
    String gender;
    String fcmToken;
}
