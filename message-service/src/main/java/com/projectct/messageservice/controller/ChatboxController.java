package com.projectct.messageservice.controller;

import com.projectct.messageservice.DTO.Message.request.LastSeenMessageRequest;
import com.projectct.messageservice.DTO.Message.response.LastSeenMessageResponse;
import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.DTO.Message.response.MessageSourceResponse;
import com.projectct.messageservice.DTO.RespondData;
import com.projectct.messageservice.service.ChatboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("chatboxes")
@RequiredArgsConstructor
public class ChatboxController {
    final ChatboxService chatboxService;

    @GetMapping("p/{projectId}")
    public ResponseEntity<?> getOlderMessageByProject(
            @RequestParam(required = false, defaultValue = "0") Long last,
            @PathVariable Long projectId,
            @RequestParam(required = false) Long taskId,
            Pageable pageable) {
        Page<MessageResponse> messages = chatboxService.getOlderMessageByProject(projectId, last, taskId, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/newer")
    public ResponseEntity<?> getNewerMessageByProject(
            @RequestParam(required = false, defaultValue = "0") Long last,
            @PathVariable Long projectId,
            @RequestParam(required = false) Long taskId,
            Pageable pageable) {
        Page<MessageResponse> messages = chatboxService.getNewerMessageByProject(projectId, last, taskId, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/media")
    public ResponseEntity<?> getMediaMessageByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) Long taskId,
            Pageable pageable) {
        Page<MessageResponse> messages = chatboxService.getMediaMessageByProject(projectId, taskId, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/m/{messageId}/media/src")
    public ResponseEntity<?> getSourceMessage(
            @PathVariable Long projectId,
            @PathVariable Long messageId,
            @RequestParam(required = false) Long taskId,
            Pageable pageable) {
        MessageSourceResponse messages = chatboxService.getSourceMessage(projectId, messageId, taskId, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/seen")
    public ResponseEntity<?> getLastSeenMessageByProject(
            @RequestParam String usernameList,
            @PathVariable Long projectId,
            @RequestParam(required = false) Long taskId) {

        List<String> usernames = Arrays.asList(usernameList.split(","));
        LastSeenMessageResponse response = chatboxService.getLastSeenMessageByProject(usernames, projectId, taskId);

        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();

        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/pin")
    public ResponseEntity<?> getPinnedMessagesByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) Long taskId,
            Pageable pageable) {
        Page<MessageResponse> messages = chatboxService.getPinnedMessagesByProject(projectId, taskId, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("search/p/{projectId}")
    public ResponseEntity<?> searchMessages(
            @PathVariable Long projectId,
            @RequestParam String keyword,
            @RequestParam String mode,
            @RequestParam(required = false) Long taskId,
            Pageable pageable) {
        Page<MessageResponse> messages = chatboxService.searchMessages(projectId, keyword, mode, taskId, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
