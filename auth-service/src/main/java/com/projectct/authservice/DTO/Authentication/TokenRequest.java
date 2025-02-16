package com.projectct.authservice.DTO.Authentication;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenRequest {
    String token;
}
