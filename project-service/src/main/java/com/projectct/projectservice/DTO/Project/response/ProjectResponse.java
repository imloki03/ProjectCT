package com.projectct.projectservice.DTO.Project.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectResponse {
    Long id;
    Long ownerId;
    String ownerUsername;
    String name;
    String description;
    String avatarURL;
    LocalDateTime createdDate;
    LocalDateTime updatedAt;

}
