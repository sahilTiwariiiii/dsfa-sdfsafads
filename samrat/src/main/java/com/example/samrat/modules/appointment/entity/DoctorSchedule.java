package com.example.samrat.modules.appointment.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "doctor_schedules")
@Getter
@Setter
public class DoctorSchedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private String dayOfWeek; // e.g., MONDAY, TUESDAY

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int avgConsultationTime; // in minutes

    @Column(nullable = false)
    private int maxPatients;

    private boolean active = true;
}
