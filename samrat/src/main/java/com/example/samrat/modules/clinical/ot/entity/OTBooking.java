package com.example.samrat.modules.clinical.ot.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ot_bookings")
@Getter
@Setter
public class OTBooking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgeon_id", nullable = false)
    private Doctor surgeon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anesthetist_id")
    private Doctor anesthetist;

    @Column(nullable = false)
    private String procedureName;

    @Column(nullable = false)
    private LocalDateTime scheduleDate;

    private Integer durationInMinutes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OTStatus status = OTStatus.SCHEDULED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private com.example.samrat.modules.admin.entity.Department department;

    private String notes;

    @Column(name = "video_recording_url")
    private String videoRecordingUrl;

    public enum OTStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
