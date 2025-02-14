package com.projectct.authservice.mapper;

import com.projectct.authservice.DTO.User.request.RegisterRequest;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);
    UserResponse toUserResponse(User user);
}
