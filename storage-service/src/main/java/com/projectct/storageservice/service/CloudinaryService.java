package com.projectct.storageservice.service;

import com.projectct.storageservice.DTO.Cloudinary.response.CloudinaryResponse;
import com.projectct.storageservice.DTO.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    UploadResponse uploadFile(MultipartFile file, boolean stored);
}
