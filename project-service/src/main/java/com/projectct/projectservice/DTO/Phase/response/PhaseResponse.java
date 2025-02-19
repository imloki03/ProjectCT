package com.projectct.projectservice.DTO.Phase.response;

import com.projectct.projectservice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhaseResponse {
    Long id;
    String name;
    String description;
    LocalDate startDate;
    LocalDate endDate;
    Status status;
    LocalDateTime createdDate;
}
