package com.projectct.projectservice.mapper;

import com.projectct.projectservice.DTO.Phase.request.PhaseRequest;
import com.projectct.projectservice.DTO.Phase.request.UpdatePhaseRequest;
import com.projectct.projectservice.DTO.Phase.response.PhaseResponse;
import com.projectct.projectservice.model.Phase;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhaseMapper {
    Phase toPhase(PhaseRequest phaseRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEditPhase(UpdatePhaseRequest updatePhaseRequest, @MappingTarget Phase phase);
    PhaseResponse toPhaseResponse(Phase phase);
    List<PhaseResponse> toPhaseResponseList(List<Phase> phaseList);
}
