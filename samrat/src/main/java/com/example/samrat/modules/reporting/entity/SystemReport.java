package com.example.samrat.modules.reporting.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_reports")
@Getter
@Setter
public class SystemReport extends BaseEntity {

    @Column(nullable = false)
    private String reportName;

    @Column(nullable = false)
    private String reportType; // FINANCIAL, CLINICAL, INVENTORY, PATIENT

    @Column(nullable = false)
    private String reportFormat; // PDF, EXCEL, CSV

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private String generatedBy;

    @Column(nullable = false)
    private String reportUrl; // Storage reference

    @Column(columnDefinition = "TEXT")
    private String reportParameters; // JSON or string of parameters used

    private String status = "COMPLETED"; // COMPLETED, FAILED, PROCESSING
}
