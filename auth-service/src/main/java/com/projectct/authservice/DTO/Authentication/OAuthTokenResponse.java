package com.projectct.authservice.DTO.Authentication;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuthTokenResponse {
    String token;
    boolean updated;
}
