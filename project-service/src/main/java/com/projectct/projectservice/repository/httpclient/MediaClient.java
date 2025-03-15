package com.projectct.projectservice.repository.httpclient;

import com.projectct.projectservice.DTO.Media.response.MediaResponse;
import com.projectct.projectservice.DTO.RespondData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "storage-service")
public interface MediaClient {
    @GetMapping(value = "/storages", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<List<MediaResponse>> getMediaList(@RequestParam List<Long> mediaIds);
}
