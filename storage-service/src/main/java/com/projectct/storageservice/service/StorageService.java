package com.projectct.storageservice.service;

import com.projectct.storageservice.DTO.Media.request.MediaRequest;
import com.projectct.storageservice.DTO.Media.response.MediaPagingResponse;
import com.projectct.storageservice.DTO.Media.response.MediaResponse;
import org.springframework.data.domain.Pageable;

public interface StorageService {
    MediaResponse addMedia(Long projectId, MediaRequest request, boolean stored);
    MediaResponse getMediaInfo(Long mediaId);
    MediaPagingResponse getStorageMedia(Long projectId, Pageable pageable);
    MediaResponse updateMediaInfo(Long mediaId, MediaRequest request);
    MediaResponse updateMediaVersion(Long mediaId, MediaRequest request);
    void deleteMedia(Long mediaId);
}
