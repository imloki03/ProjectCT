package com.projectct.storageservice.mapper;

import com.projectct.storageservice.DTO.Media.request.MediaRequest;
import com.projectct.storageservice.DTO.Media.response.MediaResponse;
import com.projectct.storageservice.model.Media;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    Media toMedia(MediaRequest request);
    MediaResponse toMediaResponse(Media media);
    List<MediaResponse> toMediaResponseList(List<Media> mediaList);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMedia(MediaRequest request, @MappingTarget Media task);

    default Long map(Media media) {
        return (media != null) ? media.getId() : null;
    }
}
