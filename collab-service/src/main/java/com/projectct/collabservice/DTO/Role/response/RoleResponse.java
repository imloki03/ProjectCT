package com.projectct.collabservice.DTO.Role.response;

import com.projectct.collabservice.DTO.Function.response.FunctionResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Long id;
    String name;
    Long projectId;
    List<FunctionResponse> functionList;
}
