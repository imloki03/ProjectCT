package com.projectct.projectservice.DTO.User.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    String name;
    String email;
    String gender;
    String avatarURL;
    List<String> fcmTokens;
    String githubId;
    Boolean isGoogleAccount;
}
