package com.projectct.projectservice.DTO.Project.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectRequest {
    String projectName;
    String projectDescription;
}
