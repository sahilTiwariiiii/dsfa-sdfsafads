package com.example.samrat.modules.clinical.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EMRRecordResponseDTO {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long departmentId;

    private String chiefComplaint;
    private String bloodPressure;
    private Double bodyTemperature;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Integer oxygenSaturation;
    private Double weight;
    private Double height;
    private Double bmi;
    private String historyOfPresentIllness;
    private String pastMedicalHistory;
    private String allergies;
    private String physicalExamination;
    private String diagnosis;
    private String prescription;
    private String labOrders;
    private String radiologyOrders;
    private String status;
}

