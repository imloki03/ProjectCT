package com.projectct.notificationservice.DTO.Email.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
     String receiver;
     String subject;
     String templateName;
     List<Object> args;
}
