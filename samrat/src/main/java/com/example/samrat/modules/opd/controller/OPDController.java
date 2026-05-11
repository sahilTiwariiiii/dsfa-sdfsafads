package com.example.samrat.modules.opd.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.opd.dto.OPDVisitDTO;
import com.example.samrat.modules.opd.entity.OPDVisit;
import com.example.samrat.modules.opd.service.OPDVisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/opd")
@RequiredArgsConstructor
@Tag(name = "V1 - opdRoute", description = "Enterprise APIs for managing outpatient visits, walk-ins, and patient vitals")
public class OPDController {

    private final OPDVisitService opdVisitService;

    // --- Enterprise OPD APIs ---

    @PostMapping("/check-in/{appointmentId}")
    @PreAuthorize("hasAuthority('OPD_WRITE')")
    @Operation(summary = "Check-in patient from appointment", description = "Checks in a patient for their scheduled OPD appointment")
    public ResponseEntity<BaseResponse<OPDVisitDTO>> checkIn(@PathVariable Long appointmentId) {
        OPDVisitDTO visit = opdVisitService.checkIn(appointmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient checked in successfully", null, visit));
    }

    @PostMapping("/walk-in")
    @PreAuthorize("hasAuthority('OPD_WRITE')")
    @Operation(summary = "Register walk-in visit", description = "Registers a new walk-in visit for a patient")
    public ResponseEntity<BaseResponse<OPDVisitDTO>> registerWalkIn(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam String visitType,
            @RequestParam String slot,
            @RequestParam Double fee) {
        OPDVisitDTO visit = opdVisitService.registerWalkInVisit(patientId, doctorId, departmentId, visitType, slot, fee);
        return ResponseEntity.ok(new BaseResponse<>(true, "Walk-in visit registered successfully", null, visit));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('OPD_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a paginated list of all OPD visits")
    public ResponseEntity<BaseResponse<Page<OPDVisitDTO>>> getAllVisits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OPDVisitDTO> visits = opdVisitService.getAllVisits(pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Visits retrieved successfully", null, visits));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('OPD_READ')")
    @Operation(summary = "Search OPD visits", description = "Filters OPD visits by patient, doctor, department, status, and date range")
    public ResponseEntity<BaseResponse<Page<OPDVisitDTO>>> searchVisits(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OPDVisitDTO> visits = opdVisitService.searchVisits(patientId, doctorId, departmentId, status, start, end, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Search results found", null, visits));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('OPD_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves details of a specific OPD visit")
    public ResponseEntity<BaseResponse<OPDVisitDTO>> getVisitById(@PathVariable Long id) {
        OPDVisitDTO visit = opdVisitService.getVisitById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Visit found", null, visit));
    }

    @PostMapping("/vitals/{opdVisitId}")
    @PreAuthorize("hasAuthority('OPD_WRITE')")
    @Operation(summary = "Record vitals", description = "Records patient vitals like weight, height, BP, temp, pulse, respiratory rate, and SpO2")
    public ResponseEntity<BaseResponse<OPDVisitDTO>> recordVitals(
            @PathVariable Long opdVisitId,
            @RequestParam Double weight,
            @RequestParam Double height,
            @RequestParam String bp,
            @RequestParam Double temp,
            @RequestParam Integer pulse,
            @RequestParam Integer resp,
            @RequestParam Integer spo2) {
        OPDVisitDTO visit = opdVisitService.recordVitals(opdVisitId, weight, height, bp, temp, pulse, resp, spo2);
        return ResponseEntity.ok(new BaseResponse<>(true, "Vitals recorded successfully", null, visit));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('OPD_WRITE')")
    @Operation(summary = "Update visit status", description = "Updates the status of an OPD visit (e.g., CALLED, IN_CONSULTATION, COMPLETED)")
    public ResponseEntity<BaseResponse<OPDVisitDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String remark) {
        OPDVisitDTO visit = opdVisitService.updateVisitStatus(id, status, remark);
        return ResponseEntity.ok(new BaseResponse<>(true, "Visit status updated successfully", null, visit));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('OPD_DELETE')")
    @Operation(summary = "Method Summary", description = "Deletes an OPD visit record")
    public ResponseEntity<BaseResponse<Void>> deleteVisit(@PathVariable Long id) {
        opdVisitService.deleteVisit(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Visit deleted successfully", null, null));
    }
}

