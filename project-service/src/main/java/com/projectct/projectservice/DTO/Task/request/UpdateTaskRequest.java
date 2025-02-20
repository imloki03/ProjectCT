package com.projectct.projectservice.DTO.Task.request;

import com.projectct.projectservice.enums.Priority;
import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.enums.TaskType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskRequest {
    String name;
    TaskType type;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Priority priority;
    List<Long> mediaIdList;
}
