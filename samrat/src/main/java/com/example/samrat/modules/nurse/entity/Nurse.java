package com.example.samrat.modules.nurse.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.admin.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "nurses")
@Getter
@Setter
public class Nurse extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false)
    private String registrationNumber;

    @Column(name = "department_id")
    private Long departmentId;

    private String experience;

    @Column(nullable = false)
    private String shift; // Morning, Evening, Night

    private boolean available = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "nurse_branches",
            joinColumns = @JoinColumn(name = "nurse_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<com.example.samrat.modules.admin.entity.Branch> branches = new HashSet<>();
}
