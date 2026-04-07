package com.example.samrat.modules.clinical.discharge.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.ipd.entity.Admission;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "discharge_summaries")
@Getter
@Setter
public class DischargeSummary extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @Column(nullable = false)
    private LocalDateTime dischargeTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String dischargeDiagnosis;

    @Column(columnDefinition = "TEXT")
    private String summaryOfCase;

    @Column(columnDefinition = "TEXT")
    private String treatmentGiven;

    @Column(columnDefinition = "TEXT")
    private String medicationsAtDischarge;

    @Column(columnDefinition = "TEXT")
    private String followUpInstructions;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DischargeType dischargeType = DischargeType.NORMAL;

    public enum DischargeType {
        NORMAL, LAMA, REFERRAL, DEATH, DISCHARGE_ON_REQUEST
    }
}
