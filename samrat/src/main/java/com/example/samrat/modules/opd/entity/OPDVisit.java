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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private com.example.samrat.modules.admin.entity.Department department;

    @Column(nullable = false)
    private LocalDateTime visitTime;

    @Column(nullable = false)
    private String tokenNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitStatus status = VisitStatus.WAITING;

    private String visitType; // OPD, Emergency, Routine
    private String slot;      // Slot I, II, III
    private Double fee;
    private String paymentMode; // Cash, Card, UPI, Insurance
    private Double discountPercent;

    // Vital signs recorded during OPD visit
    private Double weight;
    private Double height;
    private String bloodPressure;
    private Double temperature;
    private Integer pulseRate;
    private Integer respiratoryRate;
    private Integer spo2;

    private String source; // Walk-in, Online, Phone
    private String remark;
    private String notes;

    public enum VisitStatus {
        WAITING, CALLED, IN_CONSULTATION, COMPLETED, NO_SHOW, CANCELLED
    }
}
