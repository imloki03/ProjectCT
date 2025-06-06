package com.projectct.authservice.DTO.User.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStatusRequest {
    boolean isNew;
    boolean isActive;
}
