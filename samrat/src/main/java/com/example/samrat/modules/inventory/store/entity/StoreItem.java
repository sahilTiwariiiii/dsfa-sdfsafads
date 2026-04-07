package com.example.samrat.modules.inventory.store.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "store_items")
@Getter
@Setter
public class StoreItem extends BaseEntity {

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false, unique = true)
    private String itemCode;

    @Column(nullable = false)
    private String category; // MEDICAL_SUPPLY, STATIONERY, CLEANING, etc.

    @Column(nullable = false)
    private Integer currentStock;

    @Column(nullable = false)
    private Integer reorderPoint;

    @Column(nullable = false)
    private String unitOfMeasure; // PCS, BOX, PKT

    @Column(nullable = false)
    private Double averageCost;

    @Column(nullable = false)
    private boolean active = true;

    private String shelfLocation;
}
