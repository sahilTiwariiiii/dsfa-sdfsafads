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
    private String specialization;

    private String qualification;
    private String experience;

    @Column(nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private Double consultationFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private com.example.samrat.modules.admin.entity.Department department;

    private boolean available = true;

    @Column(columnDefinition = "TEXT")
    private String biography;

    private String profileImage;
    private String signatureImage;

    // Doctor can work in multiple branches of the same hospital
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "doctor_branches",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<com.example.samrat.modules.admin.entity.Branch> branches = new HashSet<>();
}
