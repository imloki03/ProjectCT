package com.projectct.collabservice.service.KafkaConsumer;


import com.projectct.collabservice.DTO.Collaborator.request.CollabRequest;
import com.projectct.collabservice.constant.KafkaTopic;
import com.projectct.collabservice.model.AppFunction;
import com.projectct.collabservice.model.Collaborator;
import com.projectct.collabservice.model.Role;
import com.projectct.collabservice.repository.AppFunctionRepository;
import com.projectct.collabservice.repository.CollaboratorRepository;
import com.projectct.collabservice.repository.RoleRepository;
import com.projectct.collabservice.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectConsumer {
    final CollaboratorRepository collaboratorRepository;
    final RoleRepository roleRepository;
    final AppFunctionRepository appFunctionRepository;
    final ObjectMapperUtil objectMapperUtil;

    @KafkaListener(topics = KafkaTopic.INIT_PROJECT_OWNER_COLLAB)
    public void createProjectOwner(String request){
        CollabRequest collabRequest = objectMapperUtil.deserializeFromJson(request,CollabRequest.class);
        List<AppFunction> appFunction = appFunctionRepository.findAll();
        Role role = Role.builder()
                .name("PROJECT_OWNER")
                .functionList(appFunction)
                .projectId(collabRequest.getProjectId())
                .build();
        roleRepository.save(role);
        collaboratorRepository.save(Collaborator.builder()
                        .projectId(collabRequest.getProjectId())
                        .userId(collabRequest.getUserId())
                        .role(role).build());
    }
}
