package com.projectct.collabservice.DTO.Role.request;

import com.projectct.collabservice.enums.FunctionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    Long projectId;
    List<Long> functionList;
}
