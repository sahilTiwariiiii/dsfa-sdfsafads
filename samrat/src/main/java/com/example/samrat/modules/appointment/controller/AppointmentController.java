package com.example.samrat.modules.appointment.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.appointment.dto.AppointmentDTO;
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
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a paginated list of all appointments")
    public ResponseEntity<BaseResponse<Page<AppointmentDTO>>> listAppointmentsV1(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment list", null, appointmentService.searchAppointments(null, null, null, null, null, null, pageable)));
    }

    // --- Enterprise Appointment APIs (v1) ---

    @PostMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_CREATE')")
    @Operation(summary = "Create a new appointment", description = "Schedules a new appointment for a patient with a specific doctor and department. Allowed visitType values: OPD, IPD, EMERGENCY")
    public ResponseEntity<BaseResponse<AppointmentDTO>> createAppointmentV1(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String visitType,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String notes) {
        AppointmentDTO appointment = appointmentService.createAppointment(patientId, doctorId, departmentId, date, visitType, priority, source, notes);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment created successfully", null, appointment));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves details of a specific appointment")
    public ResponseEntity<BaseResponse<AppointmentDTO>> getAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment found", null, appointment));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    @Operation(summary = "Update appointment", description = "Updates an existing appointment profile")
    public ResponseEntity<BaseResponse<Appointment>> updateAppointmentV1(@PathVariable Long id, @RequestBody Appointment appointment) {
        // Implementation for update would go here
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, appointment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_DELETE')")
    @Operation(summary = "Method Summary", description = "Deletes an appointment record")
    public ResponseEntity<BaseResponse<Void>> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment deleted successfully", null, null));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    @Operation(summary = "Update appointment status", description = "Updates the status of an appointment (e.g., CONFIRMED, CANCELLED, COMPLETED)")
    public ResponseEntity<BaseResponse<AppointmentDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String cancellationReason) {
        AppointmentDTO appointment = appointmentService.updateAppointmentStatus(id, status, cancellationReason);
        return ResponseEntity.ok(new BaseResponse<>(true, "Appointment status updated", null, appointment));
    }

    @GetMapping("/daily")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all appointments scheduled for a specific date")
    public ResponseEntity<BaseResponse<Page<AppointmentDTO>>> getDailyAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appointments = appointmentService.getDailyAppointments(date, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Daily appointments found", null, appointments));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all appointments for a specific patient")
    public ResponseEntity<BaseResponse<Page<AppointmentDTO>>> getPatientAppointments(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appointments = appointmentService.getAppointmentsByPatient(patientId, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient appointments found", null, appointments));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all appointments for a specific doctor")
    public ResponseEntity<BaseResponse<Page<AppointmentDTO>>> getDoctorAppointments(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appointments = appointmentService.getAppointmentsByDoctor(doctorId, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor appointments found", null, appointments));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    @Operation(summary = "Search appointments", description = "Filters appointments by patient, doctor, department, date range, and status")
    public ResponseEntity<BaseResponse<Page<AppointmentDTO>>> searchAppointments(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appointments = appointmentService.searchAppointments(patientId, doctorId, departmentId, startDate, endDate, status, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Search results found", null, appointments));
    }
}

