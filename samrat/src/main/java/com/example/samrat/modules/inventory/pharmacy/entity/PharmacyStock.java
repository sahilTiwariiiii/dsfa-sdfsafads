package com.example.samrat.modules.inventory.pharmacy.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pharmacy_stock")
@Getter
@Setter
public class PharmacyStock extends BaseEntity {

    @Column(nullable = false)
    private String medicineName;

    @Column(nullable = false, unique = true)
    private String batchNumber;

    @Column(nullable = false)
    private String genericName;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Double mrp;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Integer reorderLevel;

    @Column(nullable = false)
    private boolean active = true;

    private String category; // TABLET, SYRUP, INJECTION, etc.
}
