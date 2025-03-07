package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Phase.request.PhaseRequest;
import com.projectct.projectservice.DTO.Phase.request.UpdatePhaseRequest;
import com.projectct.projectservice.DTO.Phase.response.PhaseResponse;
import com.projectct.projectservice.constant.MessageKey;
import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.exception.AppException;
import com.projectct.projectservice.mapper.PhaseMapper;
import com.projectct.projectservice.model.Phase;
import com.projectct.projectservice.repository.PhaseRepository;
import com.projectct.projectservice.model.Project;
import com.projectct.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {
    final PhaseMapper phaseMapper;
    final ProjectRepository projectRepository;
    final PhaseRepository phaseRepository;

    @Override
    public PhaseResponse createNewPhase(Long projectId, PhaseRequest phaseRequest) {
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.PROJECT_NOT_FOUND);
        }

        Phase phase = phaseMapper.toPhase(phaseRequest);
        phase.setStatus(Status.TODO);
        phase.setCreatedDate(LocalDateTime.now());
        phase.setProject(project);
        phaseRepository.save(phase);
        return phaseMapper.toPhaseResponse(phase);
    }

    @Override
    public PhaseResponse getPhase(Long phaseId) {
        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.PHASE_NOT_FOUND);
        }
        return phaseMapper.toPhaseResponse(phase);
    }

    @Override
    public List<PhaseResponse> getAllPhase(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.PROJECT_NOT_FOUND);
        }
        List<Phase> phaseList = phaseRepository.findByProject(project);
        return phaseMapper.toPhaseResponseList(phaseList);
    }

    @Override
    public PhaseResponse updatePhase(Long phaseId, UpdatePhaseRequest updatePhaseRequest) {
        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.PHASE_NOT_FOUND);
        }
        phaseMapper.toEditPhase(updatePhaseRequest, phase);
        phaseRepository.save(phase);
        return phaseMapper.toPhaseResponse(phase);
    }

    @Transactional
    @Override
    public void deletePhase(Long phaseId) {
        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.PHASE_NOT_FOUND);
        }
        phaseRepository.delete(phase);
    }
}
