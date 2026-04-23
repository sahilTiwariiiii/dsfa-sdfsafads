package com.example.samrat.modules.clinical.emergency.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "er_visits")
@Getter
@Setter
public class ERVisit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_doctor_id")
    private Doctor assignedDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private com.example.samrat.modules.admin.entity.Department department;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TriageStatus triage = TriageStatus.GREEN;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ERStatus status = ERStatus.ADMITTED;

    private String chiefComplaint;

    public enum TriageStatus {
        RED, YELLOW, GREEN, BLACK // Standard medical triage
    }

    public enum ERStatus {
        ADMITTED, OBSERVED, DISCHARGED, REFERRED, DEATH
    }
}
