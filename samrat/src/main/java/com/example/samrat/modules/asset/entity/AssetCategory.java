package com.example.samrat.modules.asset.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "asset_categories")
@Getter
@Setter
public class AssetCategory extends BaseEntity {
    private String name;
    private String code;
    private String description;
    private String status = "ACTIVE";
}
