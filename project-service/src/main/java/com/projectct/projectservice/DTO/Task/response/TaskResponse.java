package com.projectct.projectservice.DTO.Task.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.projectct.projectservice.DTO.Media.response.MediaResponse;
import com.projectct.projectservice.enums.Priority;
import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.enums.TaskType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {
    Long id;
    String name;
    TaskType type;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    LocalDateTime createdDate;
    Priority priority;
    Status status;
    Long parentTaskId;
    Long assigneeId;
    List<MediaResponse> mediaList;
    List<MediaResponse> proofList;
    Long backlogId;
    Long phaseId;
}
