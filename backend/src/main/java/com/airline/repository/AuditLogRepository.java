package com.airline.repository;

import com.airline.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByActionContainingIgnoreCaseOrEntityNameContainingIgnoreCase(
        String action, String entityName, Pageable pageable
    );
}
