package com.example.samrat.modules.finance.insurance.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tpa_companies")
@Getter
@Setter
public class TPACompany extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String address;
    private String phone;
    private String email;

    @Column(nullable = false)
    private Double discountPercentage = 0.0;

    @Column(nullable = false)
    private Double handlingCharge = 0.0;

    @Column(nullable = false)
    private boolean active = true;

    private String contractStartDate;
    private String contractEndDate;
}
