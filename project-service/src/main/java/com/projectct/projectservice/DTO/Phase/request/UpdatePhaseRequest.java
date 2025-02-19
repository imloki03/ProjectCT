package com.projectct.projectservice.DTO.Phase.request;


import com.projectct.projectservice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePhaseRequest {
    String name;
    String description;
    LocalDate startDate;
    LocalDate endDate;
    Status status;
}
