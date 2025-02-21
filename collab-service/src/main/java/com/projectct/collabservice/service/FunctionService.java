package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.Function.request.FunctionRequest;
import com.projectct.collabservice.DTO.Function.request.FunctionUpdateRequest;
import com.projectct.collabservice.DTO.Function.response.FunctionResponse;

import java.util.List;

public interface FunctionService {
    FunctionResponse createFunction(FunctionRequest request);

    FunctionResponse updateFunction(FunctionUpdateRequest request, Long functionId);

    FunctionResponse getFunction(Long functionId);

    List<FunctionResponse> getAllFunctionOfRole(Long roleId);

    void deleteFunction(Long functionId);
}
