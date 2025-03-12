package com.projectct.storageservice.controller;

import com.projectct.storageservice.DTO.Media.request.MediaRequest;
import com.projectct.storageservice.DTO.RespondData;
import com.projectct.storageservice.constant.MessageKey;
import com.projectct.storageservice.service.StorageService;
import com.projectct.storageservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("storages")
@RequiredArgsConstructor
public class StorageController {
    final StorageService storageService;

    @PostMapping("media/{projectId}")
    public ResponseEntity<?> addMedia(@PathVariable Long projectId,
                                      @RequestBody MediaRequest request,
                                      @RequestParam(required = false, defaultValue = "false") boolean stored) {
        var media = storageService.addMedia(projectId, request, stored);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(media)
                .desc(MessageUtil.getMessage(MessageKey.MEDIA_ADD_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{mediaId}")
    public ResponseEntity<?> getMediaInfo(@PathVariable Long mediaId) {
        var media = storageService.getMediaInfo(mediaId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(media)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("all/{projectId}")
    public ResponseEntity<?> getStorageMedia(@PathVariable Long projectId, Pageable pageable) {
        var mediaPage = storageService.getStorageMedia(projectId, pageable);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(mediaPage)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }


    @PatchMapping("{mediaId}")
    public ResponseEntity<?> updateMediaInfo(@PathVariable Long mediaId, @RequestBody MediaRequest request) {
        var media = storageService.updateMediaInfo(mediaId, request);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(media)
                .desc(MessageUtil.getMessage(MessageKey.MEDIA_UPDATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping("{mediaId}")
    public ResponseEntity<?> updateMediaVersion(@PathVariable Long mediaId, @RequestBody MediaRequest request) {
        var media = storageService.updateMediaVersion(mediaId, request);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(media)
                .desc(MessageUtil.getMessage(MessageKey.MEDIA_VERSION_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @DeleteMapping("{mediaId}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long mediaId) {
        storageService.deleteMedia(mediaId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.MEDIA_DELETE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
