package com.example.samrat.modules.reception.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_logs")
@Getter
@Setter
public class VisitorLog extends BaseEntity {

    @Column(nullable = false)
    private String visitorName;

    @Column(nullable = false)
    private String phoneNumber;

    private String purpose; // e.g., Meeting, Inquiry, Patient Visit

    private String relationToPatient;

    private String patientUhid; // If visiting a patient

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private String idProofType;
    private String idProofNumber;

    @Column(name = "receptionist_id")
    private Long receptionistId;
}
