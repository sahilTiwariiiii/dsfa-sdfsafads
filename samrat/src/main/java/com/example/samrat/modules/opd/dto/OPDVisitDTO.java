package com.example.samrat.modules.opd.dto;

import com.example.samrat.modules.opd.entity.OPDVisit;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OPDVisitDTO {
    private Long id;
    private Long appointmentId;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long departmentId;
    private String departmentName;

    private LocalDateTime visitTime;
    private String tokenNumber;
    private OPDVisit.VisitStatus status;

    private String visitType;
    private String slot;
    private Double fee;
    private String paymentMode;
    private Double discountPercent;

    private Double weight;
    private Double height;
    private String bloodPressure;
    private Double temperature;
    private Integer pulseRate;
    private Integer respiratoryRate;
    private Integer spo2;

    private String source;
    private String remark;
    private String notes;
}

