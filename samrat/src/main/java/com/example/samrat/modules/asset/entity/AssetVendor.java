package com.example.samrat.modules.asset.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "asset_vendors")
@Getter
@Setter
public class AssetVendor extends BaseEntity {
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String tinNumber;
    private String status = "ACTIVE";
}
