package com.example.samrat.modules.admin.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class Department extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code; // e.g., CARD-01

    private String description;

    @Column(nullable = false)
    private boolean active = true;

    // A department belongs to a specific branch of a hospital
    // Already inherits hospitalId and branchId from BaseEntity
}
