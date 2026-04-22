package com.example.samrat.modules.diagnostics.lab.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lab_results")
@Getter
@Setter
public class LabResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @Column(nullable = false)
    private String parameterName; // e.g., Hemoglobin

    @Column(nullable = false)
    private String value; // e.g., 14.5

    private String unit; // e.g., g/dL

    private String referenceRange; // e.g., 13.5 - 17.5

    private String flag; // e.g., Normal, High, Low
}
