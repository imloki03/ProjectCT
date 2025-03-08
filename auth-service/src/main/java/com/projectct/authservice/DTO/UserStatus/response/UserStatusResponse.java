package com.projectct.authservice.DTO.UserStatus.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStatusResponse {
     boolean activated;
     boolean newUser;
}
