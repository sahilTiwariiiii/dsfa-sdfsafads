package com.example.samrat.modules.asset.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "asset_locations")
@Getter
@Setter
public class AssetLocation extends BaseEntity {
    private String name;
    private String building;
    private String floor;
    private String roomNumber;
    private String status = "ACTIVE";
}
