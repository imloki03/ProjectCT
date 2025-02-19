package com.projectct.authservice.mapper;

import com.projectct.authservice.DTO.Tag.response.TagResponse;
import com.projectct.authservice.DTO.User.request.EditProfileRequest;
import com.projectct.authservice.DTO.User.request.RegisterRequest;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tagList", ignore = true)
    void toUpdateUser(EditProfileRequest request, @MappingTarget User user);
    TagResponse toTagResponse(TagResponse response);
}
