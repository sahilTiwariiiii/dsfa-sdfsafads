package com.example.samrat.modules.support.hr.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.admin.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "employee_details")
@Getter
@Setter
public class Employee extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String employeeCode;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column(nullable = false)
    private Double basicSalary;

    @Column(nullable = false)
    private Double allowances;

    @Column(nullable = false)
    private Double deductions;

    @Column(nullable = false)
    private String panNumber;

    @Column(nullable = false)
    private String bankAccountNumber;

    @Column(nullable = false)
    private String bankIfsc;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    public enum EmployeeStatus {
        ACTIVE, RESIGNED, TERMINATED, ON_LEAVE
    }
}
