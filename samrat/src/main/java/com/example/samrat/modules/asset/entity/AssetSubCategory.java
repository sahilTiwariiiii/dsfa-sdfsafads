package com.example.samrat.modules.asset.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "asset_sub_categories")
@Getter
@Setter
public class AssetSubCategory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private AssetCategory category;

    private String name;
    private String code;
    private String description;
    private String status = "ACTIVE";
}
