package com.projectct.authservice.DTO.User.response;

import com.projectct.authservice.DTO.Authentication.AuthenticationResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    UserResponse userData;
    AuthenticationResponse token;
}
