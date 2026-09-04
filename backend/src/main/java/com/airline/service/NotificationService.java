package com.airline.service;

import com.airline.entity.Notification;
import com.airline.entity.User;
import com.airline.entity.enums.NotificationType;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.NotificationRepository;
import com.airline.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Getter
    @Builder
    public static class NotificationResponse {
        private Long id;
        private String title;
        private String message;
        private NotificationType notificationType;
        private String referenceId;
        private Boolean isRead;
        private ZonedDateTime createdAt;
    }

    @Transactional
    public void sendNotification(String userEmail, String title, String message, NotificationType type, String refId) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) return;

        Notification n = Notification.builder()
            .user(user)
            .title(title)
            .message(message)
            .notificationType(type)
            .referenceId(refId)
            .isRead(false)
            .build();

        notificationRepository.save(n);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
            .map(n -> NotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .notificationType(n.getNotificationType())
                .referenceId(n.getReferenceId())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build())
            .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId).orElse(null);
        if (n != null) {
            n.setIsRead(true);
            notificationRepository.save(n);
        }
    }
}
