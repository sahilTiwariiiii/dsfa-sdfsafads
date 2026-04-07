package com.example.samrat.modules.clinical.nursing.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.ipd.entity.Admission;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "nursing_notes")
@Getter
@Setter
public class NursingNote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @Column(nullable = false)
    private LocalDateTime noteTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    private String status; // NORMAL, URGENT, CRITICAL

    // Vital signs recorded by nurse
    private Double temperature;
    private Double pulse;
    private Double bpSystolic;
    private Double bpDiastolic;
    private Double spo2;
}
