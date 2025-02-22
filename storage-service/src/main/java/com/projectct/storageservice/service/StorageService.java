package com.projectct.storageservice.service;

import com.projectct.storageservice.DTO.Media.request.MediaRequest;
import com.projectct.storageservice.DTO.Media.response.MediaResponse;
import com.projectct.storageservice.DTO.Storage.response.StorageResponse;

import java.util.List;

public interface StorageService {
    StorageResponse createStorage(Long projectId);
    MediaResponse addMedia(Long projectId, MediaRequest request);
    MediaResponse getMediaInfo(Long mediaId);
    List<MediaResponse> getStorageMedia(Long projectId);
    MediaResponse updateMediaInfo(Long mediaId, MediaRequest request);
    MediaResponse updateMediaVersion(Long mediaId, MediaRequest request);
    void deleteMedia(Long mediaId);
}
