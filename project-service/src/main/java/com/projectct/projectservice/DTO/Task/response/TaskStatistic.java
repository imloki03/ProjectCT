package com.projectct.projectservice.DTO.Task.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskStatistic {
    Long totalTask;
    Long upcoming; //tasks in backlog
    Long inProgress; //tasks haven't done in phases
    Long completed; //task has done status
}
