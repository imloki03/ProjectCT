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
    Long toDo; //task has to do status
    Long inProgress; //task has in progress status
    Long completed; //task has done status
}
