package com.example.samrat.modules.opd.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "opd_visits")
@Getter
@Setter
public class OPDVisit extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime visitTime;

    @Column(nullable = false)
    private String tokenNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitStatus status = VisitStatus.WAITING;

    // Vital signs recorded during OPD visit
    private Double weight;
    private Double height;
    private Double bloodPressure;
    private Double temperature;
    private Double pulseRate;
    private Double respiratoryRate;
    private Double spo2;

    private String notes;

    public enum VisitStatus {
        WAITING, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
