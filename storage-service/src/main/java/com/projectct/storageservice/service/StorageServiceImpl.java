package com.projectct.storageservice.service;

import com.projectct.storageservice.DTO.Media.request.MediaRequest;
import com.projectct.storageservice.DTO.Media.response.MediaResponse;
import com.projectct.storageservice.DTO.Storage.response.StorageResponse;
import com.projectct.storageservice.constant.MessageKey;
import com.projectct.storageservice.enums.MediaType;
import com.projectct.storageservice.exception.AppException;
import com.projectct.storageservice.mapper.MediaMapper;
import com.projectct.storageservice.mapper.StorageMapper;
import com.projectct.storageservice.model.Media;
import com.projectct.storageservice.model.Storage;
import com.projectct.storageservice.repository.MediaRepository;
import com.projectct.storageservice.repository.StorageRepository;
import com.projectct.storageservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService{
    final StorageRepository storageRepository;
    final MediaRepository mediaRepository;
    final StorageMapper storageMapper;
    final MediaMapper mediaMapper;

    @Transactional
    @Override
    public MediaResponse addMedia(Long projectId, MediaRequest request, boolean stored) {
        Media media = mediaMapper.toMedia(request);
        media.setType(convertFilenameToMediaType(request.getFilename()));
        Storage storage = storageRepository.findByProjectId(projectId);
        if (storage == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.STORAGE_NOT_FOUND));
        storage.addMedia(media);
        if (stored)
            media.setStorage(storage);
        mediaRepository.save(media);
        storageRepository.save(storage);
        return mediaMapper.toMediaResponse(media);
    }

    @Override
    public MediaResponse getMediaInfo(Long mediaId) {
        Media media = mediaRepository.findById(mediaId).orElse(null);
        if (media == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.MEDIA_NOT_FOUND));
        return mediaMapper.toMediaResponse(media);
    }

    @Override
    public List<MediaResponse> getStorageMedia(Long projectId) {
        Storage storage = storageRepository.findByProjectId(projectId);
        if (storage == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.STORAGE_NOT_FOUND));
        return mediaMapper.toMediaResponseList(storage.getMediaList());
    }

    @Override
    public MediaResponse updateMediaInfo(Long mediaId, MediaRequest request) {
        Media media = mediaRepository.findById(mediaId).orElse(null);
        if (media == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.MEDIA_NOT_FOUND));
        mediaMapper.updateMedia(request, media);
        if (request.getFilename() != null)
            media.setType(convertFilenameToMediaType(request.getFilename()));
        mediaRepository.save(media);
        return mediaMapper.toMediaResponse(media);
    }

    @Override
    public MediaResponse updateMediaVersion(Long mediaId, MediaRequest request) {
        Media media = mediaRepository.findById(mediaId).orElse(null);
        if (media == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.MEDIA_NOT_FOUND));
        Media newVersion = mediaMapper.toMedia(request);
        newVersion.setType(convertFilenameToMediaType(request.getFilename()));
        newVersion.setPreviousVersion(media);
        newVersion.setStorage(media.getStorage());
        mediaRepository.save(newVersion);
        return mediaMapper.toMediaResponse(newVersion);
    }

    @Override
    public void deleteMedia(Long mediaId) {
        Media currentMedia = mediaRepository.findById(mediaId).orElse(null);
        if (currentMedia == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.MEDIA_NOT_FOUND));
        Media newerVersion = mediaRepository.findByPreviousVersion_Id(mediaId);
        Media previousVersion = currentMedia.getPreviousVersion();

        if (newerVersion != null) {
            newerVersion.setPreviousVersion(previousVersion);
            mediaRepository.saveAndFlush(newerVersion);
        }
        mediaRepository.deleteById(mediaId);
    }

    public MediaType convertFilenameToMediaType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "mp4", "avi", "mkv" -> MediaType.VIDEO;
            case "jpg", "jpeg", "png", "gif" -> MediaType.IMAGE;
            case "doc", "docx", "pdf" -> MediaType.DOC;
            case "ppt", "pptx" -> MediaType.PRESENTATION;
            case "xls", "xlsx" -> MediaType.WORKBOOK;
            default -> throw new AppException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, MessageUtil.getMessage(MessageKey.MEDIA_NOT_SUPPORT));
        };
    }
}
