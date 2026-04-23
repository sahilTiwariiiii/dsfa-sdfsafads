package com.example.samrat.modules.clinical.emr.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "emr_records")
@Getter
@Setter
public class EMRRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private com.example.samrat.modules.admin.entity.Department department;

    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;

    // Vitals
    private String bloodPressure;
    private Double bodyTemperature;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Integer oxygenSaturation; // SpO2
    private Double weight;
    private Double height;
    private Double bmi;

    @Column(columnDefinition = "TEXT")
    private String historyOfPresentIllness;

    @Column(columnDefinition = "TEXT")
    private String pastMedicalHistory;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String physicalExamination;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String prescription;

    @Column(columnDefinition = "TEXT")
    private String labOrders;

    @Column(columnDefinition = "TEXT")
    private String radiologyOrders;

    private String status = "ACTIVE"; // ACTIVE, FINALIZED
}
