package com.example.samrat.modules.clinical.vitals.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_vitals")
@Getter
@Setter
public class PatientVital extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    private Double weight;
    private Double height;
    private String bloodPressure;
    private Double temperature;
    private Integer pulseRate;
    private Integer respiratoryRate;
    private Integer spo2;

    private String recordedBy; // Name/ID of nurse or device
    private String remark;
    
    // Optional: link to visit or admission
    private Long opdVisitId;
    private Long admissionId;
}
