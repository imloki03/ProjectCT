package com.projectct.messageservice.service.KafkaConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectct.messageservice.DTO.RespondData;
import com.projectct.messageservice.constant.KafkaTopic;
import com.projectct.messageservice.model.Chatbox;
import com.projectct.messageservice.repository.ChatboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectConsumer {
    final ChatboxRepository chatboxRepository;

    @KafkaListener(topics = KafkaTopic.INIT_PROJECT)
    public void createChatBox(Long projectId){
        chatboxRepository.save(Chatbox.builder().projectId(projectId).build());
    }
}
