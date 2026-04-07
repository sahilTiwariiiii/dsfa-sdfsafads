package com.example.samrat.modules.billing.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_charges")
@Getter
@Setter
public class ServiceCharge extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category; // e.g., CONSULTATION, LAB, RADIOLOGY, OT, NURSING

    @Column(nullable = false)
    private Double standardCharge;

    private Double taxPercentage;
    private boolean active = true;
}
