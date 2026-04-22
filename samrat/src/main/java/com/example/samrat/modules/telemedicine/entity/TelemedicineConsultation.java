package com.example.samrat.modules.telemedicine.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemedicine_consultations")
@Getter
@Setter
public class TelemedicineConsultation extends BaseEntity {

    @Column(nullable = false)
    private Long appointmentId;

    @Column(nullable = false)
    private String patientUhid;

    @Column(nullable = false)
    private Long doctorId;

    private String meetingUrl; // Zoom, Google Meet, or internal link

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status = ConsultationStatus.SCHEDULED;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public enum ConsultationStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
