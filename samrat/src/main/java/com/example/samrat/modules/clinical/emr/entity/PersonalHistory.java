package com.example.samrat.modules.clinical.emr.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "personal_histories")
@Getter
@Setter
public class PersonalHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String type; // Tobacco, Alcohol, Exercise, Diet, etc.
    private String frequency;
    private String duration;
    private String status; // Current, Former, Never

    @Column(columnDefinition = "TEXT")
    private String notes;
}
