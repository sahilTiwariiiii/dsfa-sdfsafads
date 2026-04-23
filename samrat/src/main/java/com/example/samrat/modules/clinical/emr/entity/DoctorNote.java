package com.example.samrat.modules.clinical.emr.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_notes")
@Getter
@Setter
public class DoctorNote extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private String title;
    private String type; // Consultation, Follow-up, Progress, Discharge, Referral
    private LocalDateTime noteTime;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String status; // Draft, Finalized
}
