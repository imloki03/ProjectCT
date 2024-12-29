package com.projectct.authservice.DTO.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String username;
    private String name;
    private String email;
    private String gender;
    private String avatarURL;
}
