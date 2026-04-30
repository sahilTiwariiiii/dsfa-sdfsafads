package com.example.samrat.modules.ipd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request body for patient admission")
public class AdmissionRequest {

    @NotNull(message = "Patient ID is required")
    @Schema(description = "ID of the patient to admit", example = "1")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    @Schema(description = "ID of the admitting doctor", example = "1")
    private Long doctorId;

    @NotNull(message = "Bed ID is required")
    @Schema(description = "ID of the bed to allocate", example = "2")
    private Long bedId;

    @Schema(description = "Optional department ID", example = "1")
    private Long departmentId;

    @Schema(description = "Case type", example = "NORMAL", allowableValues = {"NORMAL", "MLC"})
    private String caseType;

    @Schema(description = "Triage category", example = "GREEN", allowableValues = {"RED", "YELLOW", "GREEN"})
    private String triage;

    @Schema(description = "Guardian name", example = "ipd testing")
    private String guardianName;

    @Schema(description = "Guardian phone", example = "+1234567890")
    private String guardianPhone;

    @Schema(description = "Guardian relation", example = "Father")
    private String guardianRelation;

    @Schema(description = "Insurance provider", example = "HealthFirst Insurance")
    private String insuranceProvider;

    @Schema(description = "Policy number", example = "POL12345")
    private String policyNumber;

    @Schema(description = "Diagnosis", example = "General weakness")
    private String diagnosis;

    @Schema(description = "Admission reason", example = "Routine checkup and observation")
    private String admissionReason;

    @Schema(description = "Advance amount paid", example = "5000.0")
    private Double advanceAmount;
}
