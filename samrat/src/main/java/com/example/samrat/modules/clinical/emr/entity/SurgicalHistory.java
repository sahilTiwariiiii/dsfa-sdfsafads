package com.example.samrat.modules.clinical.emr.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "surgical_histories")
@Getter
@Setter
public class SurgicalHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String surgeryName;
    private String surgeryDate;
    private String surgeon;
    private String hospital;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
