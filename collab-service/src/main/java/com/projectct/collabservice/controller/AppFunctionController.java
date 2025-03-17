package com.projectct.collabservice.controller;

import com.projectct.collabservice.DTO.Collaborator.request.CollabRoleUpdateRequest;
import com.projectct.collabservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.collabservice.DTO.Function.request.FunctionRequest;
import com.projectct.collabservice.DTO.Function.request.FunctionUpdateRequest;
import com.projectct.collabservice.DTO.Function.response.FunctionResponse;
import com.projectct.collabservice.DTO.RespondData;
import com.projectct.collabservice.constant.MessageKey;
import com.projectct.collabservice.service.FunctionService;
import com.projectct.collabservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("functions")
@RequiredArgsConstructor
public class AppFunctionController {
    final FunctionService functionService;

    @PostMapping
    public ResponseEntity<?> createFunction(@RequestBody FunctionRequest request){
        FunctionResponse response = functionService.createFunction(request);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .desc(MessageUtil.getMessage(MessageKey.FUNCTION_CREATE_SUCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("/{functionId}")
    public ResponseEntity<?> getFunctionById(@PathVariable Long functionId) {
        FunctionResponse response = functionService.getFunction(functionId); // Single FunctionResponse
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
        return ResponseEntity.ok(respondData);
    }

    @GetMapping("/r/{roleId}")
    public ResponseEntity<?> getFunctionsByRole(@PathVariable Long roleId) {
        List<FunctionResponse>  responses = functionService.getAllFunctionOfRole(roleId); // List<FunctionResponse>
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();
        return ResponseEntity.ok(respondData);
    }

    @PutMapping("{functionId}")
    public ResponseEntity<?> updateFunction(@RequestBody FunctionUpdateRequest request, @PathVariable Long functionId) {
        FunctionResponse response = functionService.updateFunction(request, functionId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .desc(MessageUtil.getMessage(MessageKey.FUNCTION_UPDATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @DeleteMapping("{functionId}")
    public ResponseEntity<?> deleteFunction(@PathVariable Long functionId) {
        functionService.deleteFunction(functionId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.FUNCTION_DELETE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllFunctionAvailable() {
        var functions = functionService.getAllFunctionAvailable();
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(functions)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
