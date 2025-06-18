package com.projectct.notificationservice.service.KafkaConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectct.notificationservice.DTO.Email.request.EmailRequest;
import com.projectct.notificationservice.constant.KafkaTopic;
import com.projectct.notificationservice.util.EmailUtil;
import com.projectct.notificationservice.util.ObjectMapperUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConsumer {
    final EmailUtil emailUtil;
    final ObjectMapperUtil objectMapperUtil;

    @KafkaListener(topics = KafkaTopic.SEND_EMAIL)
    public void sendEmail(String request) {
        EmailRequest requestEmail = objectMapperUtil.deserializeFromJson(request, EmailRequest.class);
        try {
            emailUtil.sendEmail(requestEmail.getReceiver(), requestEmail.getSubject(), requestEmail.getTemplateName(), requestEmail.getArgs().toArray());
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
