package com.projectct.messageservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.projectct.messageservice.DTO.Gemini.request.MsgTextListRequest;
import com.projectct.messageservice.DTO.RespondData;
import com.projectct.messageservice.service.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("gemini")
@RequiredArgsConstructor
public class GeminiController {
    final GeminiService geminiService;

    @PostMapping
    public ResponseEntity<?> assistantRequest(@RequestBody MsgTextListRequest request) {
        String messageRes = geminiService.sendRequest(request.getMessages());
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(messageRes)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
