package com.projectct.notificationservice.controller;

import com.projectct.notificationservice.DTO.FCMToken.request.FCMTokenRequest;
import com.projectct.notificationservice.DTO.RespondData;
import com.projectct.notificationservice.DTO.Subcribe.request.SubscriptionRequest;
import com.projectct.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
public class NotificationController {
    final NotificationService notificationService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeToTopics(@RequestBody SubscriptionRequest request) {
        String result = notificationService.subscribeToTopics(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(result)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribeToTopics(@RequestBody SubscriptionRequest request) {
        String result = notificationService.unsubscribeToTopic(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(result)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping("{notificationId}")
    public ResponseEntity<?> readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
