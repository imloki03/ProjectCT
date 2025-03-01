package com.projectct.notificationservice.DTO.Subcribe.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionRequest {
    private List<String> topics;
    private String token;
}
