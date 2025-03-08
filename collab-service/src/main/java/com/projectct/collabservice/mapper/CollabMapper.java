package com.projectct.collabservice.mapper;

import com.projectct.collabservice.DTO.Collaborator.request.CollabRequest;
import com.projectct.collabservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.collabservice.DTO.Collaborator.response.CollabWithoutUserResponse;
import com.projectct.collabservice.model.Collaborator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface CollabMapper {
    Collaborator toCollaborator(CollabRequest request);
    @Mapping(source = "role", target = "role")
    CollabResponse toResponse(Collaborator collaborator);
    CollabWithoutUserResponse toResponseWithoutUser(Collaborator collaborator);
    List<CollabResponse> toResponseList(List<Collaborator> collaborators);
}
