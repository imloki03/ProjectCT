package com.projectct.storageservice.service.KafkaConsumer;

import com.projectct.storageservice.constant.KafkaTopic;
import com.projectct.storageservice.model.Storage;
import com.projectct.storageservice.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectConsumer {
    final StorageRepository storageRepository;

    @KafkaListener(topics = KafkaTopic.INIT_PROJECT)
    public void createStorage(Long projectId){
        storageRepository.save(Storage.builder().projectId(projectId).build());
    }
}
