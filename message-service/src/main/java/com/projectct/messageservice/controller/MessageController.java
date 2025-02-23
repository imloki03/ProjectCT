package com.projectct.messageservice.controller;

import com.projectct.messageservice.DTO.Message.request.MessageRequest;
import com.projectct.messageservice.DTO.Message.request.PinMessageRequest;
import com.projectct.messageservice.DTO.Message.request.ReadMessageRequest;
import com.projectct.messageservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("messages")
@RequiredArgsConstructor
public class MessageController {
    final MessageService messageService;
    @MessageMapping("/send")
    public void sendMessage(MessageRequest request) {
        messageService.sendMessage(request);
    }

    @MessageMapping("/pin")
    public void pinMessage(PinMessageRequest request) {
        messageService.pinMessage(request);
    }

    @MessageMapping("/read")
    public void readMessage(ReadMessageRequest request) {
        messageService.readMessage(request);
    }

    @MessageMapping("/typing")
    public void typingMessage(MessageRequest request) {
        messageService.typingMessage(request);
    }
}
