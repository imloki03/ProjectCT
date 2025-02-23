package com.projectct.storageservice.DTO.Media.response;

import com.projectct.storageservice.DTO.UploadResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaResponse implements UploadResponse {
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
