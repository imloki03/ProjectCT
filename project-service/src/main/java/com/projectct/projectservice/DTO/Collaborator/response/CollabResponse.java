package com.projectct.projectservice.DTO.Collaborator.response;
import com.projectct.projectservice.DTO.User.response.UserResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollabResponse {
    Long id;
    Long userId;
    UserResponse user;
    Long projectId;
}
