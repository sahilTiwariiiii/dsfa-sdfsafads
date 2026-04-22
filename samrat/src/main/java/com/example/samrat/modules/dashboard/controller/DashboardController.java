package com.example.samrat.modules.dashboard.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.dashboard.dto.DoctorDashboardDTO;
import com.example.samrat.modules.dashboard.dto.PatientPortalDTO;
import com.example.samrat.modules.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard & Portal", description = "APIs for aggregated data views for Doctors and Patients")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Get Doctor Dashboard", description = "Aggregates appointments, surgeries, and clinical data for a doctor")
    public ResponseEntity<BaseResponse<DoctorDashboardDTO>> getDoctorDashboard(@PathVariable Long doctorId) {
        DoctorDashboardDTO dashboard = dashboardService.getDoctorDashboard(doctorId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor dashboard data found", null, dashboard));
    }

    @GetMapping("/patient/{uhid}")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    @Operation(summary = "Get Patient Portal Data", description = "Aggregates medical records, appointments, and billing for a patient")
    public ResponseEntity<BaseResponse<PatientPortalDTO>> getPatientPortal(@PathVariable String uhid) {
        PatientPortalDTO portal = dashboardService.getPatientPortalData(uhid);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient portal data found", null, portal));
    }
}
