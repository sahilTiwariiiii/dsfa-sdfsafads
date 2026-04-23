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
@Table(name = "clinical_prescriptions")
@Getter
@Setter
public class ClinicalPrescription extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private OPDVisit visit;

    private String medicineName;
    private String dosage; // e.g., 500mg
    private String frequency; // e.g., 1-0-1
    private String route; // Oral, IV, Topical
    private String duration; // 5 days
    private String instructions; // After meal, Before sleep
    private String refillCount;
    private LocalDateTime prescriptionTime;

    private String status = "ACTIVE"; // ACTIVE, CANCELLED, COMPLETED
}
