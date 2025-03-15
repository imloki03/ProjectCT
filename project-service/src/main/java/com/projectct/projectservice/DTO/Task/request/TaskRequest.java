package com.projectct.projectservice.DTO.Task.request;

import com.projectct.projectservice.enums.Priority;
import com.projectct.projectservice.enums.TaskType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRequest {
    String name;
    TaskType type;
    String description;
    Priority priority;
    Long parentTaskId;
    List<Long> mediaIdList;
}
