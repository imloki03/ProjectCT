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
    private Long id;
    private Long ownerId;
    private String ownerUsername;
    private String name;
    private String description;
    private String avatarURL;
    private LocalDateTime createdDate;
}
