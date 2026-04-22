package com.example.samrat.modules.opd.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.opd.entity.OPDVisit;
import com.example.samrat.modules.opd.service.OPDVisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/opd")
@RequiredArgsConstructor
@Tag(name = "OPD Management", description = "APIs for managing outpatient visits and patient vitals")
public class OPDController {

    private final OPDVisitService opdVisitService;

    @PostMapping("/check-in/{appointmentId}")
    @PreAuthorize("hasAuthority('OPD_WRITE')")
    @Operation(summary = "Check-in patient", description = "Checks in a patient for their scheduled OPD appointment")
    public ResponseEntity<BaseResponse<OPDVisit>> checkIn(@PathVariable Long appointmentId) {
        OPDVisit visit = opdVisitService.checkIn(appointmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient checked in successfully", null, visit));
    }

    @PostMapping("/vitals/{opdVisitId}")
    @PreAuthorize("hasAuthority('OPD_WRITE')")
    @Operation(summary = "Record vitals", description = "Records patient vitals like weight, height, BP, etc., during an OPD visit")
    public ResponseEntity<BaseResponse<OPDVisit>> recordVitals(
            @PathVariable Long opdVisitId,
            @RequestParam Double weight,
            @RequestParam Double height,
            @RequestParam Double bp,
            @RequestParam Double temp,
            @RequestParam Double pulse) {
        OPDVisit visit = opdVisitService.recordVitals(opdVisitId, weight, height, bp, temp, pulse);
        return ResponseEntity.ok(new BaseResponse<>(true, "Vitals recorded successfully", null, visit));
    }
}
