package com.example.samrat.modules.patient.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
public class Patient extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String phoneNumber;

    private String email;
    private String address;

    @Column(nullable = false, unique = true)
    private String uhid; // Unique Health ID

    private String maritalStatus;
    private String bloodGroup;
    private String guardianName;
    private String guardianPhone;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String occupation;
    private String religion;
    private String nationality;
    private String patientImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_head_id")
    private Patient familyHead; // For family mapping

    private String relationshipWithHead; // e.g., SON, DAUGHTER, SPOUSE

    @Column(nullable = false)
    private boolean active = true;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
