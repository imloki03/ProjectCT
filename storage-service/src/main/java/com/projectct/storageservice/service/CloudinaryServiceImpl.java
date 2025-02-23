package com.projectct.storageservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.projectct.storageservice.DTO.Cloudinary.response.CloudinaryResponse;
import com.projectct.storageservice.DTO.UploadResponse;
import com.projectct.storageservice.mapper.MediaMapper;
import com.projectct.storageservice.model.Media;
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
    final MediaMapper mediaMapper;
    @Override
    public UploadResponse uploadFile(MultipartFile file, boolean stored) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");
            if (stored) {
                Media newMedia = Media.builder()
                        .link(url)
                        .build();
                mediaRepository.save(newMedia);
                return mediaMapper.toMediaResponse(newMedia);
            }
            return CloudinaryResponse.builder()
                    .url(url)
                    .publicId(publicId)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
