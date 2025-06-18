package com.projectct.projectservice.DTO.Email.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
     String receiver;
     String subject;
     String templateName;
     List<Object> args;
}
