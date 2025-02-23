package com.projectct.storageservice.DTO.Cloudinary.response;

import com.projectct.storageservice.DTO.UploadResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudinaryResponse implements UploadResponse {
    String publicId;
    String url;
}
