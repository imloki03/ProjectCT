package com.projectct.authservice.DTO.User.response;

import com.projectct.authservice.DTO.Tag.response.TagResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    String name;
    String email;
    String gender;
    String avatarURL;
    List<TagResponse> tagList;
}
