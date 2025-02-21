package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.Role.request.RoleRequest;
import com.projectct.collabservice.DTO.Role.request.RoleUpdateRequest;
import com.projectct.collabservice.DTO.Role.response.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);

    Object getRole(Long roleId);

    Object getAllRoleOfProject(Long projectId);

    RoleResponse updateRole(RoleUpdateRequest request, Long roleId);

    void deleteRole(Long roleId);
}
