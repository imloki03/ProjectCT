package com.projectct.projectservice.controller;

import com.projectct.projectservice.DTO.Phase.request.PhaseRequest;
import com.projectct.projectservice.DTO.Phase.request.UpdatePhaseRequest;
import com.projectct.projectservice.DTO.Phase.response.PhaseResponse;
import com.projectct.projectservice.DTO.RespondData;
import com.projectct.projectservice.service.PhaseService;
import com.projectct.projectservice.util.MessageUtil;
import com.projectct.projectservice.constant.MessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("phases")
@RequiredArgsConstructor
public class PhaseController {
    final PhaseService phaseService;

    @PostMapping("{projectId}")
    public ResponseEntity<?> createNewPhase(@PathVariable Long projectId, @RequestBody PhaseRequest phaseRequest){
        PhaseResponse phaseResponse = phaseService.createNewPhase(projectId, phaseRequest);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.PHASE_CREATE_SUCCESS))
                .data(phaseResponse)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{phaseId}")
    public ResponseEntity<?> getPhase(@PathVariable Long phaseId) {
        PhaseResponse phaseResponse = phaseService.getPhase(phaseId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(phaseResponse)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("/pid/{projectId}")
    public ResponseEntity<?> getAllPhase(@PathVariable Long projectId){
        List<PhaseResponse> phaseResponses = phaseService.getAllPhase(projectId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(phaseResponses)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping("{phaseId}")
    public ResponseEntity<?> updatePhase( @PathVariable Long phaseId, @RequestBody UpdatePhaseRequest updatePhaseRequest) {
        PhaseResponse phaseResponse = phaseService.updatePhase(phaseId, updatePhaseRequest);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(phaseResponse)
                .desc(MessageUtil.getMessage(MessageKey.PHASE_UPDATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @DeleteMapping("{phaseId}")
    public ResponseEntity<?> deletePhase(@PathVariable Long phaseId) {
        phaseService.deletePhase(phaseId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.PHASE_DELETE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

}
