package com.projectct.collabservice.controller;

import com.projectct.collabservice.DTO.Collaborator.request.CollabRequest;
import com.projectct.collabservice.DTO.Collaborator.request.CollabRoleUpdateRequest;
import com.projectct.collabservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.collabservice.DTO.Collaborator.response.CollabWithoutUserResponse;
import com.projectct.collabservice.DTO.Notification.request.DirectNotificationRequest;
import com.projectct.collabservice.DTO.RespondData;
import com.projectct.collabservice.constant.MessageKey;
import com.projectct.collabservice.service.CollaboratorService;
import com.projectct.collabservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("collabs")
@RequiredArgsConstructor
public class CollaboratorController {
    final CollaboratorService collaboratorService;

    @PostMapping
    public ResponseEntity<?> createCollab(@RequestBody CollabRequest collabRequest){
        collaboratorService.createCollab(collabRequest);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.COLLAB_ADD_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{collabId}")
    public ResponseEntity<?> getCollab(@PathVariable Long collabId) {
        CollabResponse collabResponse = collaboratorService.getCollab(collabId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(collabResponse)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("current/p/{currentProjectId}")
    public ResponseEntity<?> getCurrentCollab(@PathVariable Long currentProjectId) {
        CollabWithoutUserResponse collabResponse = collaboratorService.getCurrentCollab(currentProjectId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(collabResponse)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}")
    public ResponseEntity<?> getAllCollabFromProject(@PathVariable Long projectId) {
        List<CollabResponse> collabResponses = collaboratorService.getAllCollabFromProject(projectId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(collabResponses)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("collab/{userId}")
    public ResponseEntity<?> getAllCollabProject(@PathVariable Long userId) {
        var collabList = collaboratorService.getAllCollabProject(userId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(collabList)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("cid/{userId}")
    public ResponseEntity<?> getAllCollabIdList(@PathVariable Long userId) {
        var collabList = collaboratorService.getAllCollabIdList(userId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(collabList)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("{collabId}")
    public ResponseEntity<?> updateCollabRole(@RequestBody CollabRoleUpdateRequest request, @PathVariable Long collabId) {
        collaboratorService.updateCollabRole(request, collabId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.COLLAB_UPDATE_ROLE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @DeleteMapping("{collabId}")
    public ResponseEntity<?> deleteCollaborator(@PathVariable Long collabId) {
        collaboratorService.deleteCollaborator(collabId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.COLLAB_DELETE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

}
