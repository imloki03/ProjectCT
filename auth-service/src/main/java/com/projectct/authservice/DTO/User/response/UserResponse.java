package com.projectct.authservice.DTO.User.response;

import com.projectct.authservice.DTO.Tag.response.TagResponse;
import com.projectct.authservice.DTO.UserStatus.response.UserStatusResponse;
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
    List<TagResponse> tagList;
    UserStatusResponse status;
}
