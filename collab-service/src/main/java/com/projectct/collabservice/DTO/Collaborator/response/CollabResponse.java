package com.projectct.collabservice.DTO.Collaborator.response;

import com.projectct.collabservice.DTO.Role.response.RoleResponse;
import com.projectct.collabservice.DTO.response.UserResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollabResponse {
    Long id;
    UserResponse user;
    Long projectId;
    RoleResponse role;
}
