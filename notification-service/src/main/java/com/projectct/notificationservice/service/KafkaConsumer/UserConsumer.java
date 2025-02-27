package com.projectct.notificationservice.service.KafkaConsumer;

import com.projectct.notificationservice.constant.KafkaTopic;
import com.projectct.notificationservice.model.NotificationQueue;
import com.projectct.notificationservice.repository.NotificationQueueRepository;
import com.projectct.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumer {
    final NotificationQueueRepository notificationQueueRepository;

    @KafkaListener(topics = KafkaTopic.INIT_USER)
    public void createNotificationQueue(Long userId){
        notificationQueueRepository.save(NotificationQueue.builder().userId(userId).build());
    }
}
