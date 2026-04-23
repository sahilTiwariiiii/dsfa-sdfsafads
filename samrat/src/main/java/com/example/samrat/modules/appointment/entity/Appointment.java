package com.example.samrat.modules.appointment.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.admin.entity.Department;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Column(nullable = false)
    private String tokenNumber; // For queue management

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentPriority priority = AppointmentPriority.NORMAL;

    private String chiefComplaint; // Reason for visit

    @Column(nullable = false)
    private String visitType; // OPD, EMERGENCY, FOLLOW_UP

    private String source; // Walk-in, Online, Phone

    private boolean isBilled = false;

    private String notes;

    private String cancellationReason;

    public enum AppointmentStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW, IN_CONSULTATION
    }

    public enum AppointmentPriority {
        NORMAL, URGENT, EMERGENCY
    }
}
