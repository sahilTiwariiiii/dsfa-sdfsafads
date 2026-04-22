package com.example.samrat.modules.compliance.service;

import com.example.samrat.modules.compliance.entity.AuditLog;
import com.example.samrat.modules.compliance.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComplianceService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(String username, String action, String module, String details, String ipAddress) {
        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setModule(module);
        log.setDetails(details);
        log.setIpAddress(ipAddress);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
