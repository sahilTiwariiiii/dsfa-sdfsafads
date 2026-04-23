package com.example.samrat.modules.asset.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.admin.entity.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "assets")
@Getter
@Setter
public class Asset extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private AssetCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private AssetSubCategory subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    private String name;
    private String assetCode;
    private String serialNumber;
    private String modelNumber;
    private String manufacturer;
    
    private LocalDate purchaseDate;
    private Double purchaseCost;
    private LocalDate warrantyExpiry;
    
    private String status; // AVAILABLE, ASSIGNED, UNDER_MAINTENANCE, DISPOSED
    
    @Column(columnDefinition = "TEXT")
    private String description;
}
