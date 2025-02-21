package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.Collaborator.request.CollabRequest;
import com.projectct.collabservice.DTO.Collaborator.request.CollabRoleUpdateRequest;
import com.projectct.collabservice.DTO.Collaborator.response.CollabResponse;

import java.util.List;

public interface CollaboratorService {
    void createCollab(CollabRequest collabRequest);

    List<CollabResponse> getAllCollabFromProject(Long projectId);

    void updateCollabRole(CollabRoleUpdateRequest request, Long collabId);

    void deleteCollaborator(Long collabId);

    CollabResponse getCollab(Long collabId);
}
