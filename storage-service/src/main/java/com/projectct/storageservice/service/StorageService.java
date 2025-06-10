package com.projectct.storageservice.service;

import com.projectct.storageservice.DTO.Media.request.MediaRequest;
import com.projectct.storageservice.DTO.Media.response.MediaPagingResponse;
import com.projectct.storageservice.DTO.Media.response.MediaResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StorageService {
    MediaResponse addMedia(Long projectId, MediaRequest request, boolean stored);
    MediaResponse getMediaInfo(Long mediaId);
    List<MediaResponse> getMediaList(List<Long> mediaIds);
    MediaPagingResponse getStorageMedia(Long projectId, Pageable pageable);
    MediaResponse updateMediaInfo(Long mediaId, MediaRequest request);
    MediaResponse updateMediaVersion(Long mediaId, MediaRequest request);
    void deleteMedia(Long mediaId);
    void addMediaFromChatToStorage(Long projectId, Long mediaId);
    MediaPagingResponse searchMedia(Long projectId, String keyword, Pageable pageable);
}
