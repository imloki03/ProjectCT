package com.projectct.authservice.mapper;
import com.projectct.authservice.DTO.Tag.response.TagResponse;
import com.projectct.authservice.model.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    List<TagResponse> toTagResponseList(List<Tag> tags);
}
