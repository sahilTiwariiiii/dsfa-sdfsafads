package com.example.samrat.modules.appointment.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "V1 - appointmentRoute", description = "Enterprise APIs for scheduling and managing patient appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    @Operation(summary = "List V1 - appointmentRoute")
    public ResponseEntity<BaseResponse<Page<Appointment>>> listAppointmentsV1(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment list", null, appointmentService.searchAppointments(null, null, null, null, null, null, pageable)));
    }

    @PostMapping("/v1-create") // Changed to avoid conflict with existing @PostMapping
    @Operation(summary = "Create V1 - appointmentRoute")
    public ResponseEntity<BaseResponse<Appointment>> createAppointmentV1Generic(@RequestBody Appointment appointment) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, appointment));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update V1 - appointmentRoute")
    public ResponseEntity<BaseResponse<Appointment>> updateAppointmentV1(@PathVariable Long id, @RequestBody Appointment appointment) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, appointment));
    }

    @DeleteMapping("/{id}/v1-delete") // Changed to avoid conflict with existing @DeleteMapping
    @Operation(summary = "Delete V1 - appointmentRoute")
    public ResponseEntity<BaseResponse<Void>> deleteAppointmentV1(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Deleted", null, null));
    }

    // --- Enterprise Appointment APIs (v1) ---

    @PostMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_CREATE')")
    @Operation(summary = "Create a new appointment", description = "Schedules a new appointment for a patient with a specific doctor and department")
    public ResponseEntity<BaseResponse<Appointment>> createAppointmentV1(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String visitType,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String notes) {
        Appointment appointment = appointmentService.createAppointment(patientId, doctorId, departmentId, date, visitType, priority, source, notes);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment created successfully", null, appointment));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Get appointment by ID", description = "Retrieves details of a specific appointment")
    public ResponseEntity<BaseResponse<Appointment>> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment found", null, appointment));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    @Operation(summary = "Update appointment status", description = "Updates the status of an appointment (e.g., CONFIRMED, CANCELLED, COMPLETED)")
    public ResponseEntity<BaseResponse<Appointment>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String cancellationReason) {
        Appointment appointment = appointmentService.updateAppointmentStatus(id, status, cancellationReason);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment status updated", null, appointment));
    }

    @GetMapping("/daily")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Get daily appointments", description = "Retrieves all appointments scheduled for a specific date")
    public ResponseEntity<BaseResponse<Page<Appointment>>> getDailyAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentService.getDailyAppointments(date, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Daily appointments found", null, appointments));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Get patient appointments", description = "Retrieves all appointments for a specific patient")
    public ResponseEntity<BaseResponse<Page<Appointment>>> getPatientAppointments(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient appointments found", null, appointments));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Get doctor appointments", description = "Retrieves all appointments for a specific doctor")
    public ResponseEntity<BaseResponse<Page<Appointment>>> getDoctorAppointments(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor appointments found", null, appointments));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Search appointments", description = "Filters appointments by patient, doctor, department, date range, and status")
    public ResponseEntity<BaseResponse<Page<Appointment>>> searchAppointments(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentService.searchAppointments(patientId, doctorId, departmentId, startDate, endDate, status, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Search results found", null, appointments));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_DELETE')")
    @Operation(summary = "Delete appointment", description = "Deletes an appointment record")
    public ResponseEntity<BaseResponse<Void>> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment deleted successfully", null, null));
    }
}
