package com.projectct.authservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    final private KafkaTemplate<String, Object> kafkaTemplate;
    final private ObjectMapper objectMapper;

    public void sendMessage(String topic, Object message) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
