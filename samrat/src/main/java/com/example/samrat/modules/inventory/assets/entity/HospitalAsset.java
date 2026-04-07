package com.example.samrat.modules.inventory.assets.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "hospital_assets")
@Getter
@Setter
public class HospitalAsset extends BaseEntity {

    @Column(nullable = false)
    private String assetName;

    @Column(nullable = false, unique = true)
    private String assetTag;

    @Column(nullable = false)
    private String category; // MEDICAL_EQUIPMENT, FURNITURE, IT_EQUIPMENT, VEHICLE

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private Double purchaseValue;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetStatus status = AssetStatus.IN_USE;

    private LocalDate lastServiceDate;
    private LocalDate nextServiceDate;

    public enum AssetStatus {
        IN_USE, UNDER_MAINTENANCE, REPAIR_NEEDED, SCRAPPED, DISPOSED
    }
}
