package com.example.samrat.modules.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    private String experience;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotNull(message = "Consultation fee is required")
    private Double consultationFee;

    private Long departmentId;
    private String departmentName;

    private boolean available = true;

    private String fullName; // From user
    private String email;    // From user
    private String phoneNumber; // From user

    private String biography;
    private String profileImage;
    private String signatureImage;
}
