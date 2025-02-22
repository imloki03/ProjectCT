package com.projectct.storageservice.DTO.Storage.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StorageResponse {
    Long id;
    Long projectId;
}
