package com.projectct.collabservice.controller;

import com.projectct.collabservice.DTO.Function.request.FunctionRequest;
import com.projectct.collabservice.DTO.Function.request.FunctionUpdateRequest;
import com.projectct.collabservice.DTO.Function.response.FunctionResponse;
import com.projectct.collabservice.DTO.RespondData;
import com.projectct.collabservice.DTO.Role.request.RoleRequest;
import com.projectct.collabservice.DTO.Role.request.RoleUpdateRequest;
import com.projectct.collabservice.DTO.Role.response.RoleResponse;
import com.projectct.collabservice.constant.MessageKey;
import com.projectct.collabservice.service.RoleService;
import com.projectct.collabservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {
    final RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request){
        RoleResponse response = roleService.createRole(request);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .desc(MessageUtil.getMessage(MessageKey.ROLE_CREATE_SUCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{roleId}")
    public ResponseEntity<?> getRole(@PathVariable Long roleId) {
        var result = roleService.getRole(roleId); // Single RoleResponse
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(result)
                .build();
        return ResponseEntity.ok(respondData);
    }


    @GetMapping("p/{projectId}")
    public ResponseEntity<?> getRolesByProject(@PathVariable Long projectId) {
        var result = roleService.getAllRoleOfProject(projectId); // List<RoleResponse>
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(result)
                .build();
        return ResponseEntity.ok(respondData);
    }

    @PutMapping("{roleId}")
    public ResponseEntity<?> updateRole(@RequestBody RoleUpdateRequest request, @PathVariable Long roleId) {
        RoleResponse response = roleService.updateRole(request, roleId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .desc(MessageUtil.getMessage(MessageKey.ROLE_UPDATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @DeleteMapping("{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.ROLE_DELETE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
