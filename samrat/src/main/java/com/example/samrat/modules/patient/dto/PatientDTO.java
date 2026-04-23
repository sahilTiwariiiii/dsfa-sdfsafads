package com.example.samrat.modules.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Data Transfer Object for Patient information")
public class PatientDTO {
    
    @Schema(description = "Internal database ID", example = "1")
    private Long id;

    @NotBlank(message = "First name is required")
    @Schema(description = "Patient first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Patient last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Gender is required")
    @Schema(description = "Patient gender", example = "Male", allowableValues = {"Male", "Female", "Other"})
    private String gender;

    @NotNull(message = "Date of birth is required")
    @Schema(description = "Patient date of birth", example = "1990-01-01")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required")
    @Schema(description = "Patient primary contact number", example = "+919876543210")
    private String phoneNumber;

    @Schema(description = "Patient email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Patient residential address", example = "123 Main St, Bhopal")
    private String address;

    @Schema(description = "Unique Hospital ID (UHID)", example = "UHID123456")
    private String uhid;

    @Schema(description = "Marital Status", example = "Single")
    private String maritalStatus;

    @Schema(description = "Blood Group", example = "O+")
    private String bloodGroup;

    @Schema(description = "Guardian Name", example = "Jane Doe")
    private String guardianName;

    @Schema(description = "Guardian Phone", example = "+919876543211")
    private String guardianPhone;

    @Schema(description = "Emergency Contact Name", example = "Jane Doe")
    private String emergencyContactName;

    @Schema(description = "Emergency Contact Phone", example = "+919876543211")
    private String emergencyContactPhone;

    @Schema(description = "Patient Occupation", example = "Software Engineer")
    private String occupation;

    @Schema(description = "Religion", example = "None")
    private String religion;

    @Schema(description = "Nationality", example = "Indian")
    private String nationality;

    @Schema(description = "URL or base64 of patient image", example = "null")
    private String patientImage;

    @Schema(description = "ID of the family head if applicable", example = "null")
    private Long familyHeadId; // For family mapping
    
    @Schema(description = "Relationship with family head", example = "Self")
    private String relationshipWithHead;
    
    @Schema(description = "Indicates if the patient profile is active", example = "true")
    private boolean active = true;
}
