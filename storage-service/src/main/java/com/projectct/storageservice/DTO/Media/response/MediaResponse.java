package com.projectct.storageservice.DTO.Media.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaResponse {
    Long id;
    String name;
    String description;
    String filename;
    String size;
    LocalDateTime uploadTime;
    String type;
    String link;
    Long previousVersion;
}
