package com.projectct.collabservice.DTO.Function.request;

import com.projectct.collabservice.enums.FunctionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FunctionUpdateRequest {
    String name;
    FunctionType type;
    String endpoint;
}
