package com.projectct.storageservice.mapper;

import com.projectct.storageservice.DTO.Storage.response.StorageResponse;
import com.projectct.storageservice.model.Storage;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Configuration;

@Mapper(componentModel = "spring")
public interface StorageMapper {
    StorageResponse toStorageResponse(Storage storage);
}
