package com.projectct.collabservice.DTO.Collaborator.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollabRoleUpdateRequest {
    Long roleId;
}
