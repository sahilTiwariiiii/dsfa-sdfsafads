package com.example.samrat.modules.ipd.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "beds")
@Getter
@Setter
public class Bed extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @Column(nullable = false)
    private String bedNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BedStatus status = BedStatus.AVAILABLE;

    @Column(nullable = false)
    private Double bedChargePerDay;

    public enum BedStatus {
        AVAILABLE, OCCUPIED, RESERVED, CLEANING, UNDER_MAINTENANCE
    }
}
