package com.example.samrat.modules.diagnostics.bloodbank.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_donations")
@Getter
@Setter
public class BloodDonation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Patient donor;

    @Column(nullable = false)
    private String bloodGroup;

    @Column(nullable = false)
    private Double volumeInMl;

    @Column(nullable = false)
    private LocalDateTime donationDate;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false, unique = true)
    private String bagId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodStatus status = BloodStatus.AVAILABLE;

    public enum BloodStatus {
        AVAILABLE, RESERVED, ISSUED, EXPIRED, DISCARDED
    }
}
