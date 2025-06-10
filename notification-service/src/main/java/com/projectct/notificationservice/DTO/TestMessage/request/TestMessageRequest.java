package com.projectct.notificationservice.DTO.TestMessage.request;

import lombok.Data;

@Data
public class TestMessageRequest {
    private String topic;
    private String title;
    private String body;
}