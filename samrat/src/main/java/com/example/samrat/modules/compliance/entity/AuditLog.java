package com.example.samrat.modules.compliance.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String action; // e.g., READ_EMR, DELETE_PATIENT, LOGIN

    @Column(nullable = false)
    private String module;

    private String details;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String ipAddress;
}
