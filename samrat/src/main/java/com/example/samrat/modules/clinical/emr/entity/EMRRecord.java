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

    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(columnDefinition = "TEXT")
    private String historyOfPresentIllness;

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
