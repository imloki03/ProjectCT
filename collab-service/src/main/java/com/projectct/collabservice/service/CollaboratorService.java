package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.Collaborator.request.CollabRequest;
import com.projectct.collabservice.DTO.Collaborator.request.CollabRoleUpdateRequest;
import com.projectct.collabservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.collabservice.DTO.Collaborator.response.CollabWithoutUserResponse;
import com.projectct.collabservice.DTO.Notification.request.DirectNotificationRequest;

import java.util.List;

public interface CollaboratorService {
    void createCollab(CollabRequest collabRequest);

    List<CollabResponse> getAllCollabFromProject(Long projectId);
    List<Long> getAllCollabProject(Long userId);

    List<Long> getAllCollabIdList(Long userId);

    void updateCollabRole(CollabRoleUpdateRequest request, Long collabId);

    void deleteCollaborator(Long collabId);

    CollabResponse getCollab(Long collabId);

    void inviteCollaborator(DirectNotificationRequest userId);

    CollabWithoutUserResponse getCurrentCollab(Long currentProjectId);
}
