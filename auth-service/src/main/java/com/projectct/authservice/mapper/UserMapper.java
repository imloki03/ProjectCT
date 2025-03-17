package com.projectct.authservice.mapper;

import com.projectct.authservice.DTO.Tag.response.TagResponse;
import com.projectct.authservice.DTO.User.request.EditProfileRequest;
import com.projectct.authservice.DTO.User.request.RegisterRequest;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.DTO.UserStatus.response.UserStatusResponse;
import com.projectct.authservice.model.User;
import com.projectct.authservice.model.UserStatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tagList", ignore = true)
    void toUpdateUser(EditProfileRequest request, @MappingTarget User user);
}
