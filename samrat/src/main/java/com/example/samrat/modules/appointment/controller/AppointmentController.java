package com.example.samrat.modules.appointment.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management", description = "APIs for scheduling and managing patient appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_CREATE')")
    @Operation(summary = "Create a new appointment", description = "Schedules a new appointment for a patient with a specific doctor")
    public ResponseEntity<BaseResponse<Appointment>> createAppointment(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String visitType) {
        Appointment appointment = appointmentService.createAppointment(patientId, doctorId, date, visitType);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment created successfully", null, appointment));
    }

    @GetMapping("/daily")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Get daily appointments", description = "Retrieves all appointments scheduled for a specific date")
    public ResponseEntity<BaseResponse<List<Appointment>>> getDailyAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Appointment> appointments = appointmentService.getDailyAppointments(date);
        return ResponseEntity.ok(new BaseResponse<>(true, "Daily appointments found", null, appointments));
    }
}
