package com.projectct.collabservice.DTO.Function.response;

import com.projectct.collabservice.enums.FunctionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FunctionResponse {
    Long id;
    String name;
    FunctionType type;
    String endpoint;
}
