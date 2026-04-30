package com.example.samrat.modules.clinical.vitals.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PatientVitalDTO {
    private Long id;
    private Long patientId;
    private LocalDateTime recordedAt;
    
    private Double weight;
    private Double height;
    private String bloodPressure;
    private Double temperature;
    private Integer pulseRate;
    private Integer respiratoryRate;
    private Integer spo2;
    
    private String recordedBy;
    private String remark;
    private Long opdVisitId;
    private Long admissionId;
}
