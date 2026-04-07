package com.example.samrat.modules.inventory.cssd.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cssd_cycles")
@Getter
@Setter
public class CSSDCycle extends BaseEntity {

    @Column(nullable = false)
    private String loadName; // e.g., OT Set A

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(nullable = false)
    private String sterilizationMethod; // STEAM, ETO, PLASMA

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double pressure;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CSSDStatus status = CSSDStatus.IN_PROGRESS;

    private String result; // PASS, FAIL

    @Column(nullable = false, unique = true)
    private String cycleNumber;

    public enum CSSDStatus {
        IN_PROGRESS, COMPLETED, CANCELLED
    }
}
