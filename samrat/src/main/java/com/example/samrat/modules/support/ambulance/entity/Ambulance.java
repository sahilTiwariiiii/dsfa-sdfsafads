package com.example.samrat.modules.support.ambulance.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ambulances")
@Getter
@Setter
public class Ambulance extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String type; // ALS (Advanced Life Support), BLS (Basic Life Support), PATIENT_TRANSPORT

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AmbulanceStatus status = AmbulanceStatus.AVAILABLE;

    private String driverName;
    private String driverPhone;

    @Column(nullable = false)
    private boolean active = true;

    public enum AmbulanceStatus {
        AVAILABLE, BUSY, UNDER_MAINTENANCE, OUT_OF_SERVICE
    }
}
