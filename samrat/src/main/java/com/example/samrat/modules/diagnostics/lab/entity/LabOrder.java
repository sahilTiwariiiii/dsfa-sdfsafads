package com.example.samrat.modules.diagnostics.lab.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_orders")
@Getter
@Setter
public class LabOrder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordering_doctor_id", nullable = false)
    private Doctor orderingDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private com.example.samrat.modules.admin.entity.Department department;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private String testName;

    @Column(nullable = false)
    private String testCode;

    private String sampleType; // BLOOD, URINE, etc.

    @Column(nullable = false)
    private LocalDateTime orderTime;

    private LocalDateTime sampleCollectionTime;
    private LocalDateTime resultTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LabStatus status = LabStatus.ORDERED;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    public enum LabStatus {
        ORDERED, SAMPLE_COLLECTED, PROCESSING, COMPLETED, CANCELLED
    }
}
