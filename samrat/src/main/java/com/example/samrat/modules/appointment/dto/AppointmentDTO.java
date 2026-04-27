package com.example.samrat.modules.appointment.dto;

import com.example.samrat.modules.appointment.entity.Appointment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Schema(description = "Appointment response payload (DTO). Avoids exposing Hibernate lazy proxies.")
public class AppointmentDTO {
    private Long id;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long departmentId;
    private String departmentName;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String tokenNumber;

    private Appointment.AppointmentStatus status;
    private Appointment.AppointmentPriority priority;

    private String visitType;
    private String source;
    private boolean billed;
    private String notes;
    private String cancellationReason;
}

