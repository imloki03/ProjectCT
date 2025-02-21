package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.Role.request.RoleRequest;
import com.projectct.collabservice.DTO.Role.request.RoleUpdateRequest;
import com.projectct.collabservice.DTO.Role.response.RoleResponse;
import com.projectct.collabservice.constant.MessageKey;
import com.projectct.collabservice.exception.AppException;
import com.projectct.collabservice.mapper.RoleMapper;
import com.projectct.collabservice.model.AppFunction;
import com.projectct.collabservice.model.Role;
import com.projectct.collabservice.repository.AppFunctionRepository;
import com.projectct.collabservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    final RoleRepository roleRepository;
    final AppFunctionRepository functionRepository;
    final RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        if (request.getFunctionList() != null) {
            role.setFunctionList(functionRepository.findAllById(request.getFunctionList()));
        }
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse getRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.ROLE_NOT_FOUND);
        }
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoleOfProject(Long projectId) {
        List<Role> roleList = roleRepository.findByProjectId(projectId);
        return roleMapper.toRoleResponseList(roleList);
    }

    @Override
    public RoleResponse updateRole(RoleUpdateRequest request, Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.ROLE_NOT_FOUND);
        }
        roleMapper.toUpdateRole(request, role);
        if (request.getFunctionList() != null) {
            role.setFunctionList(functionRepository.findAllById(request.getFunctionList()));
        }
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.ROLE_NOT_FOUND);
        }
        roleRepository.deleteById(roleId);
    }
}
