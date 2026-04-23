package com.example.samrat.modules.diagnostics.radiology.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "radiology_orders")
@Getter
@Setter
public class RadiologyOrder extends BaseEntity {

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
    private String modality; // MRI, CT, X-RAY, ULTRASOUND

    @Column(nullable = false)
    private String procedureName;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    private LocalDateTime procedureTime;
    private LocalDateTime reportTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RadiologyStatus status = RadiologyStatus.ORDERED;

    @Column(columnDefinition = "TEXT")
    private String report;

    @Column(columnDefinition = "TEXT")
    private String impression;

    private String imagesUrl; // Reference to PACS

    public enum RadiologyStatus {
        ORDERED, SCHEDULED, PERFORMED, COMPLETED, CANCELLED
    }
}
