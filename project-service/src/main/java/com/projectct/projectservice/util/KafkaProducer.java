package com.projectct.projectservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    final private KafkaTemplate<String, Object> kafkaTemplate;
    final ObjectMapperUtil objectMapperUtil ;

    public void sendMessage(String topic, Object message) {
        String jsonResponse = objectMapperUtil.toJson(message);
        kafkaTemplate.send(topic, jsonResponse);
    }
}
