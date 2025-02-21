package com.projectct.collabservice.mapper;

import com.projectct.collabservice.DTO.Function.response.FunctionResponse;
import com.projectct.collabservice.DTO.Role.request.RoleRequest;
import com.projectct.collabservice.DTO.Role.request.RoleUpdateRequest;
import com.projectct.collabservice.DTO.Role.response.RoleResponse;
import com.projectct.collabservice.model.AppFunction;
import com.projectct.collabservice.model.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = FunctionMapper.class)
public interface RoleMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "functionList", ignore = true)
    Role toRole(RoleRequest roleRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "functionList", ignore = true)
    void toUpdateRole(RoleUpdateRequest roleRequest, @MappingTarget Role role);

    @Mapping(source = "functionList", target = "functionList")
    RoleResponse toRoleResponse(Role role);
    List<RoleResponse> toRoleResponseList(List<Role> roles);
}
