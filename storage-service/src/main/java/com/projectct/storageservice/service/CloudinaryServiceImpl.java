package com.projectct.storageservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.projectct.storageservice.DTO.Cloudinary.response.CloudinaryResponse;
import com.projectct.storageservice.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    final Cloudinary cloudinary;
    final MediaRepository mediaRepository;
    @Override
    public CloudinaryResponse uploadFile(MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            log.error(result.toString());
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder()
                    .url(url)
                    .publicId(publicId)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
