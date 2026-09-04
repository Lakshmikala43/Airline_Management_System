package com.airline.dto;

import lombok.*;
import java.time.ZonedDateTime;

public class AuditLogDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuditLogResponse {
        private Long id;
        private String userEmail;
        private String action;
        private String entityName;
        private String entityId;
        private String description;
        private String ipAddress;
        private ZonedDateTime createdAt;
    }
}
