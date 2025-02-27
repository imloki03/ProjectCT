package com.projectct.messageservice.controller;

import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.DTO.RespondData;
import com.projectct.messageservice.constant.MessageKey;
import com.projectct.messageservice.repository.ChatboxRepository;
import com.projectct.messageservice.service.ChatboxService;
import com.projectct.messageservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("chatboxes")
@RequiredArgsConstructor
public class ChatboxController {
    final ChatboxService chatboxService;

    @GetMapping("p/{projectId}")
    public ResponseEntity<?> getMessagesByProject(
                            @PathVariable Long projectId,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<MessageResponse> messages = chatboxService.getMessagesByProject(projectId, page, size);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(messages)
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
