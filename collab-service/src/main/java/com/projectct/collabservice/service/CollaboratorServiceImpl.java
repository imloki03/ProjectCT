package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.Collaborator.request.CollabRequest;
import com.projectct.collabservice.DTO.Collaborator.request.CollabRoleUpdateRequest;
import com.projectct.collabservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.collabservice.DTO.Notification.request.DirectNotificationRequest;
import com.projectct.collabservice.DTO.User.response.UserResponse;
import com.projectct.collabservice.constant.KafkaTopic;
import com.projectct.collabservice.constant.MessageKey;

import com.projectct.collabservice.constant.NotificationType;
import com.projectct.collabservice.exception.AppException;
import com.projectct.collabservice.mapper.CollabMapper;
import com.projectct.collabservice.model.Collaborator;
import com.projectct.collabservice.model.Role;
import com.projectct.collabservice.repository.CollaboratorRepository;
import com.projectct.collabservice.repository.RoleRepository;
import com.projectct.collabservice.repository.httpclient.AuthClient;
import com.projectct.collabservice.util.KafkaProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollaboratorServiceImpl implements CollaboratorService{
    final CollaboratorRepository collaboratorRepository;
    final RoleRepository roleRepository;
    final CollabMapper collabMapper;
    final AuthClient authClient;
    final CachedUserService cachedUserService;
    final FCMTokenCacheService fcmTokenCacheService;
    final KafkaProducer kafkaProducer;

    @Override
    public void createCollab(CollabRequest collabRequest) {
        Collaborator collaborator = collaboratorRepository.findByProjectIdAndUserId(collabRequest.getProjectId(), collabRequest.getUserId());
        if (collaborator!=null) {
            throw new AppException(HttpStatus.CONFLICT, MessageKey.COLLAB_ADD_DUPLICATED);
        }

        Collaborator newCollab = collabMapper.toCollaborator(collabRequest);
        collaboratorRepository.save(newCollab);
    }

    @Override
    public List<CollabResponse> getAllCollabFromProject(Long projectId) {
        List<Collaborator> collaborators = collaboratorRepository.findByProjectId(projectId);
        List<Long> userIds = collaborators.stream().map(Collaborator::getUserId).toList();

        Map<Long, UserResponse> userMap = cachedUserService.getUserList(userIds);
        return collaborators.stream().map(c -> {
            CollabResponse response = collabMapper.toResponse(c);
            response.setUser(userMap.get(c.getUserId()));
            return response;
        }).toList();
    }


    @Override
    public CollabResponse getCollab(Long collabId) {
        Collaborator collaborator = collaboratorRepository.findById(collabId).orElse(null);
        if (collaborator==null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.COLLAB_NOT_FOUND);
        }
        CollabResponse collabResponse = collabMapper.toResponse(collaborator);
        collabResponse.setUser(cachedUserService.getUserInfo(collaborator.getUserId()));
        return collabResponse;
    }

    @Override
    public void inviteCollaborator(DirectNotificationRequest request) {
        request.setToken(fcmTokenCacheService.getFCMToken(request.getRecipient()).replaceAll("^\"|\"$", ""));
        request.setType(NotificationType.INVITE_NOTIFICATION);
        kafkaProducer.sendMessage(KafkaTopic.USER_DIRECT_NOTIFICATION, request);
    }

    @Override
    public void updateCollabRole(CollabRoleUpdateRequest request, Long collabId) {
        Role role = roleRepository.findById(request.getRoleId()).orElse(null);
        if (role==null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.ROLE_NOT_FOUND);
        }
        Collaborator collaborator = collaboratorRepository.findById(collabId).orElse(null);
        if (collaborator==null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.COLLAB_NOT_FOUND);
        }
        collaborator.setRole(role);
        collaboratorRepository.save(collaborator);
    }

    @Transactional
    @Override
    //xóa task
    public void deleteCollaborator(Long collabId) {
        Collaborator collaborator = collaboratorRepository.findById(collabId).orElse(null);
        if (collaborator==null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.COLLAB_NOT_FOUND);
        }
        collaboratorRepository.delete(collaborator);
    }
}
