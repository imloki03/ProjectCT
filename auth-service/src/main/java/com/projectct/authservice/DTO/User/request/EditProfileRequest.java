package com.projectct.authservice.DTO.User.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditProfileRequest {
     String name;
     String gender;
     List<Long> tagList;
}
