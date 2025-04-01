package com.projectct.messageservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectct.messageservice.DTO.Gemini.request.GeminiRequest;
import com.projectct.messageservice.DTO.Gemini.request.Part;
import com.projectct.messageservice.DTO.Gemini.request.TextPart;
import com.projectct.messageservice.configuration.GeminiConfig;
import com.projectct.messageservice.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiClassImpl implements GeminiService{
    final GeminiConfig geminiConfig;

    @Override
    public String sendRequest(List<String> messages) {
        WebClient webClient = geminiConfig.webClient();

        List<TextPart> textParts = new ArrayList<>();

        String knowledge = FileUtils.readResourceFile("gemini_knowledge.txt");
        textParts.add(TextPart.builder()
                .text(knowledge)
                .build());

        for (String message : messages) {
            TextPart textPart = TextPart
                    .builder()
                    .text(message)
                    .build();
            textParts.add(textPart);
        }

        Part part = Part.builder()
                .parts(textParts)
                .build();

        GeminiRequest request = GeminiRequest
                .builder()
                .contents(List.of(part))
                .build();

        String response = webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
