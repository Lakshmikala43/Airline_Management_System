package com.airline.service;

import com.airline.dto.AuditLogDTOs;
import com.airline.entity.AuditLog;
import com.airline.entity.User;
import com.airline.repository.AuditLogRepository;
import com.airline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public void logAction(String userEmail, String action, String entityName, String entityId, String description, String ipAddress) {
        User user = userEmail != null ? userRepository.findByEmail(userEmail).orElse(null) : null;

        AuditLog log = AuditLog.builder()
            .user(user)
            .userEmail(userEmail)
            .action(action)
            .entityName(entityName)
            .entityId(entityId)
            .description(description)
            .ipAddress(ipAddress != null ? ipAddress : "127.0.0.1")
            .build();

        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogDTOs.AuditLogResponse> getAuditLogs(String search, Pageable pageable) {
        Page<AuditLog> logs;
        if (search != null && !search.isBlank()) {
            logs = auditLogRepository.findByActionContainingIgnoreCaseOrEntityNameContainingIgnoreCase(search, search, pageable);
        } else {
            logs = auditLogRepository.findAll(pageable);
        }

        return logs.map(log -> AuditLogDTOs.AuditLogResponse.builder()
            .id(log.getId())
            .userEmail(log.getUserEmail())
            .action(log.getAction())
            .entityName(log.getEntityName())
            .entityId(log.getEntityId())
            .description(log.getDescription())
            .ipAddress(log.getIpAddress())
            .createdAt(log.getCreatedAt())
            .build());
    }
}
