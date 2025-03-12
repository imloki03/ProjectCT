package com.projectct.storageservice.DTO.Media.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaPagingResponse {
    Page<MediaResponse> mediaList;
    List<MediaResponse> additionalMediaList;
}
