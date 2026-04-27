package com.example.samrat.modules.clinical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request body for creating a clinical (EMR) record")
public class CreateClinicalDetailsRequest {

    @NotNull(message = "patientId is required")
    @Schema(description = "Patient ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long patientId;

    @NotNull(message = "doctorId is required")
    @Schema(description = "Doctor ID", example = "21", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long doctorId;

    @Schema(description = "Department ID", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long departmentId;

    @Schema(description = "Chief complaint", example = "Fever with cough for 3 days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String chiefComplaint;

    @Schema(description = "Blood pressure", example = "120/80", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bloodPressure;

    @Schema(description = "Body temperature in Celsius", example = "98.6", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double bodyTemperature;

    @Schema(description = "Heart rate (bpm)", example = "78", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer heartRate;

    @Schema(description = "Respiratory rate (breaths/min)", example = "16", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer respiratoryRate;

    @Schema(description = "Oxygen saturation (SpO2 %)", example = "98", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer oxygenSaturation;

    @Schema(description = "Weight in kg", example = "72.4", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double weight;

    @Schema(description = "Height in cm", example = "172", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double height;

    @Schema(description = "Body mass index", example = "24.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double bmi;

    @Schema(description = "History of present illness", example = "Intermittent fever, mild breathlessness", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String historyOfPresentIllness;

    @Schema(description = "Past medical history", example = "Type 2 diabetes for 5 years", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String pastMedicalHistory;

    @Schema(description = "Known allergies", example = "Penicillin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String allergies;

    @Schema(description = "Physical examination findings", example = "Mild wheeze on auscultation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String physicalExamination;

    @Schema(description = "Provisional/final diagnosis", example = "Acute bronchitis", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String diagnosis;

    @Schema(description = "Prescription notes", example = "Paracetamol 650 mg SOS", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String prescription;

    @Schema(description = "Lab orders", example = "CBC, CRP", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String labOrders;

    @Schema(description = "Radiology orders", example = "Chest X-ray PA view", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String radiologyOrders;

    @Schema(description = "Record status", example = "ACTIVE", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String status;
}

