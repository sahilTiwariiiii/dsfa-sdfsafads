package com.example.samrat.modules.doctor.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.admin.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Getter
@Setter
public class Doctor extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String specialization; // or use a separate entity

    private String qualification;
    private String experience; // e.g., "10 years"

    @Column(nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private Double consultationFee;

    @Column(name = "department_id")
    private Long departmentId;

    private boolean available = true;

    // Doctor can work in multiple branches of the same hospital
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "doctor_branches",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<com.example.samrat.modules.admin.entity.Branch> branches = new HashSet<>();
}
