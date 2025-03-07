package com.projectct.projectservice.DTO.Collab.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollabRequest {
    Long projectId;
    Long userId;
}
