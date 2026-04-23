package com.example.samrat.modules.clinical.emr.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.opd.entity.OPDVisit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_diagnoses")
@Getter
@Setter
public class ClinicalDiagnosis extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private OPDVisit visit;

    private String icdCode;
    private String diagnosisName;
    private String type; // Provisional, Final, Admitting, Discharge
    private String severity; // Mild, Moderate, Severe
    private String status; // Active, Resolved, Chronic
    private LocalDateTime diagnosisTime;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
