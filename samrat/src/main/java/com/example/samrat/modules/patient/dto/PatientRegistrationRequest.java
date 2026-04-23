package com.example.samrat.modules.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Request body for patient registration and visit")
public class PatientRegistrationRequest {

    @NotNull(message = "Visit Date is required")
    @Schema(description = "Date of the visit", example = "2026-03-09T12:24:51.597Z")
    private LocalDateTime visitDate;

    @NotBlank(message = "Visit Time is required")
    @Schema(description = "Time of the visit", example = "2026-03-09T12:24:51.598Z")
    private String visitTime;

    @NotBlank(message = "Visit Type is required")
    @Schema(description = "Type of visit", example = "OPD", allowableValues = {"OPD", "IPD", "Emergency"})
    private String visitType;

    @NotNull(message = "Fee is required")
    @Schema(description = "Consultation fee", example = "1.0")
    private Double fee;

    @NotBlank(message = "Mobile number is required")
    @Schema(description = "Patient mobile number", example = "9876543210")
    private String mobile;

    @Email(message = "Invalid email format")
    @Schema(description = "Patient email address", example = "rahul.sharma@example.com")
    private String email;

    @Schema(description = "Department ID", example = "698334ced5bdf65d67c809bd")
    private String departmentId;

    @Schema(description = "Department name", example = "General Medicine")
    private String departmentName;

    @Schema(description = "Doctor ID", example = "6981937c68d0f7fe64da7b51")
    private String doctorId;

    @NotBlank(message = "Slot is required")
    @Schema(description = "Appointment slot", example = "Slot I", allowableValues = {"Slot I", "Slot II", "Slot III"})
    private String slot;

    @NotBlank(message = "Patient name is required")
    @Schema(description = "Full name of the patient", example = "Rahul Sharma")
    private String patientName;

    @Schema(description = "Gender", example = "Male", allowableValues = {"Male", "Female", "Other"})
    private String gender;

    @Schema(description = "Marital Status", example = "Single", allowableValues = {"Single", "Married", "Divorced", "Widowed"})
    private String maritalStatus;

    @Schema(description = "Date of birth", example = "1995-05-10")
    private String dob;

    @Schema(description = "Age", example = "30")
    private Integer age;

    @Schema(description = "Current Age", example = "30")
    private Integer currentAge;

    @Schema(description = "Relation Type", example = "Father")
    private String relationType;

    @Schema(description = "Guardian Name", example = "Ramesh Sharma")
    private String guardianName;

    @Schema(description = "Address", example = "MP Nagar Zone 2")
    private String address;

    @Schema(description = "Country", example = "India")
    private String country;

    @Schema(description = "State ID", example = "507f1f77bcf86cd799439011")
    private String stateId;

    @Schema(description = "City ID", example = "507f1f77bcf86cd799439011")
    private String cityId;

    @Schema(description = "Blood Group", example = "O+")
    private String bloodGroup;

    @Schema(description = "Source", example = "Walk-in")
    private String source;

    @Schema(description = "Referred Doctor ID", example = "6981937c68d0f7fe64da7b51")
    private String referredDoctorId;

    @Schema(description = "Referral Mobile", example = "9876543210")
    private String referralMobile;

    @Schema(description = "Payment Mode", example = "Cash", allowableValues = {"Cash", "Card", "UPI", "Insurance"})
    private String paymentMode;

    @Schema(description = "Discount Percentage", example = "0.0")
    private Double discountPercent;

    @Schema(description = "Remark", example = "Regular OPD consultation")
    private String remark;

    @Schema(description = "Patient Image URL or base64", example = "")
    private String patientImage;
}
