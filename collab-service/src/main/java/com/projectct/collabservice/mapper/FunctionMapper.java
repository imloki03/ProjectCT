package com.projectct.collabservice.mapper;

import com.projectct.collabservice.DTO.Function.request.FunctionRequest;
import com.projectct.collabservice.DTO.Function.request.FunctionUpdateRequest;
import com.projectct.collabservice.DTO.Function.response.FunctionResponse;
import com.projectct.collabservice.model.AppFunction;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FunctionMapper {
    @Mapping(source = "type", target = "functionType")
    AppFunction toFunction(FunctionRequest functionRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateFunction(FunctionUpdateRequest functionUpdateRequest, @MappingTarget AppFunction appFunction);

    @Mapping(source = "functionType", target = "type")
    FunctionResponse toFunctionResponse(AppFunction appFunction);
    List<FunctionResponse> toFunctionResponseList(List<AppFunction> functions);
}
