package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Phase.request.PhaseRequest;
import com.projectct.projectservice.DTO.Phase.request.UpdatePhaseRequest;
import com.projectct.projectservice.DTO.Phase.response.PhaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PhaseService {

    PhaseResponse createNewPhase(Long projectId, PhaseRequest phaseRequest);

    PhaseResponse getPhase(Long phaseId);

    List<PhaseResponse> getAllPhase(Long projectId);

    PhaseResponse updatePhase(Long phaseId, UpdatePhaseRequest updatePhaseRequest);

    void deletePhase(Long phaseId);
}
