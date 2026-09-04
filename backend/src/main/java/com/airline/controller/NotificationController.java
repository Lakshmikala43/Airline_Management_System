package com.airline.controller;

import com.airline.security.SecurityUtils;
import com.airline.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationService.NotificationResponse>> getMyNotifications() {
        String email = SecurityUtils.getCurrentUserEmail()
            .orElseThrow(() -> new IllegalStateException("Authentication required"));
        return ResponseEntity.ok(notificationService.getUserNotifications(email));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
