package com.projectct.projectservice.DTO.Task.request;

import com.projectct.projectservice.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskStatusRequest {
    Status status;
    List<Long> proofList;
}
