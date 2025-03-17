package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.Function.request.FunctionRequest;
import com.projectct.collabservice.DTO.Function.request.FunctionUpdateRequest;
import com.projectct.collabservice.DTO.Function.response.FunctionResponse;
import com.projectct.collabservice.constant.MessageKey;
import com.projectct.collabservice.exception.AppException;
import com.projectct.collabservice.mapper.FunctionMapper;
import com.projectct.collabservice.model.AppFunction;
import com.projectct.collabservice.model.Role;
import com.projectct.collabservice.repository.AppFunctionRepository;
import com.projectct.collabservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FunctionServiceImpl implements FunctionService {
    final AppFunctionRepository appFunctionRepository;
    final RoleRepository roleRepository;
    final FunctionMapper functionMapper;

    @Override
    public FunctionResponse createFunction(FunctionRequest request) {
        AppFunction function = functionMapper.toFunction(request);
        appFunctionRepository.save(function);
        return functionMapper.toFunctionResponse(function);
    }

    @Override
    public FunctionResponse updateFunction(FunctionUpdateRequest request, Long functionId) {
        AppFunction function = appFunctionRepository.findById(functionId).orElse(null);
        if (function == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.FUNCTION_NOT_FOUND);
        }
        functionMapper.toUpdateFunction(request, function);
        if (request.getType() != null) {
            function.setFunctionType(request.getType());
        }
        appFunctionRepository.save(function);
        return functionMapper.toFunctionResponse(function);
    }

    @Override
    public FunctionResponse getFunction(Long functionId) {
        AppFunction function = appFunctionRepository.findById(functionId).orElse(null);
        if (function == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.FUNCTION_NOT_FOUND);
        }
        return functionMapper.toFunctionResponse(function);
    }

    @Override
    public List<FunctionResponse> getAllFunctionOfRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.ROLE_NOT_FOUND);
        }
        List<AppFunction> functionList = role.getFunctionList();
        return functionMapper.toFunctionResponseList(functionList);
    }

    @Override
    public void deleteFunction(Long functionId) {
        AppFunction function = appFunctionRepository.findById(functionId).orElse(null);
        if (function == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.FUNCTION_NOT_FOUND);
        }
        appFunctionRepository.delete(function);
    }

    @Override
    public List<FunctionResponse> getAllFunctionAvailable() {
        return functionMapper.toFunctionResponseList(
                appFunctionRepository.findAll()
        );
    }
}
