package com.example.samrat.modules.asset.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "asset_maintenances")
@Getter
@Setter
public class AssetMaintenance extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private AssetVendor vendor;

    private String maintenanceType; // PREVENTIVE, CORRECTIVE
    private LocalDate maintenanceDate;
    private LocalDate nextDueDate;
    private Double cost;
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED
    
    @Column(columnDefinition = "TEXT")
    private String findings;
    
    @Column(columnDefinition = "TEXT")
    private String actionTaken;
}
