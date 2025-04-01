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
                            @PathVariable Long projectId, Pageable pageable) {
        Page<MessageResponse> messages = chatboxService.getOlderMessageByProject(projectId, last, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/newer")
    public ResponseEntity<?> getNewerMessageByProject(
            @RequestParam(required = false, defaultValue = "0") Long last,
            @PathVariable Long projectId, Pageable pageable) {
        Page<MessageResponse> messages = chatboxService.getNewerMessageByProject(projectId, last, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/media")
    public ResponseEntity<?> getMediaMessageByProject(
            @PathVariable Long projectId, Pageable pageable) {
        Page<MessageResponse> messages = chatboxService.getMediaMessageByProject(projectId, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/m/{messageId}/media/src")
    public ResponseEntity<?> getSourceMessage(
            @PathVariable Long projectId, @PathVariable Long messageId, Pageable pageable) {
        MessageSourceResponse messages = chatboxService.getSourceMessage(projectId, messageId, pageable);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("p/{projectId}/seen")
    public ResponseEntity<?> getLastSeenMessageByProject(
            @RequestParam String usernameList, @PathVariable Long projectId) {

        List<String> usernames = Arrays.asList(usernameList.split(","));
        LastSeenMessageResponse response = chatboxService.getLastSeenMessageByProject(usernames, projectId);

        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();

        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("pin/p/{projectId}")
    public ResponseEntity<?> getPinnedMessagesByProject(@PathVariable Long projectId) {
        List<MessageResponse> messages = chatboxService.getPinnedMessagesByProject(projectId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages) // Include pagination metadata if needed
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("search/p/{projectId}")
    public ResponseEntity<?> searchMessages(@PathVariable Long projectId, @RequestParam String keyword) {
        List<MessageResponse> messages = chatboxService.searchMessages(projectId, keyword);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
