package com.projectct.storageservice.controller;

import com.projectct.storageservice.DTO.RespondData;
import com.projectct.storageservice.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {
    final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestPart MultipartFile file) {
        var cloudinary = cloudinaryService.uploadFile(file);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(cloudinary)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
