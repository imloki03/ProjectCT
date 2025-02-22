package com.projectct.storageservice.service;

import com.projectct.storageservice.DTO.Cloudinary.response.CloudinaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    CloudinaryResponse uploadFile(MultipartFile file);
}
