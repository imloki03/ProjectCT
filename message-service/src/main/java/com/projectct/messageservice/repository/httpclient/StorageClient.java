package com.projectct.messageservice.repository.httpclient;

import com.projectct.messageservice.DTO.Media.response.MediaResponse;
import com.projectct.messageservice.DTO.RespondData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "storage-service")
public interface StorageClient {
    @GetMapping(value = "/storages/{mediaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<MediaResponse> getMediaInfo(@PathVariable Long mediaId);
}
