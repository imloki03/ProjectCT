package com.projectct.projectservice.DTO.Phase.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhaseRequest {
    String name;
    String description;
    LocalDate startDate;
    LocalDate endDate;
}
