package com.example.samrat.modules.diagnostics.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.diagnostics.lab.entity.LabOrder;
import com.example.samrat.modules.diagnostics.service.DiagnosticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diagnostics")
@RequiredArgsConstructor
@Tag(name = "Diagnostics Management", description = "APIs for laboratory and radiology diagnostic orders")
public class DiagnosticsController {

    private final DiagnosticsService diagnosticsService;

    @PostMapping("/lab-order")
    @PreAuthorize("hasAuthority('DIAGNOSTICS_WRITE')")
    @Operation(summary = "Create lab order", description = "Schedules a new laboratory test for a patient")
    public ResponseEntity<BaseResponse<LabOrder>> createLabOrder(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam String testName,
            @RequestParam String remarks) {
        LabOrder order = diagnosticsService.createLabOrder(patientId, doctorId, testName, remarks);
        return ResponseEntity.ok(new BaseResponse<>(true, "Lab order created successfully", null, order));
    }

    @PostMapping("/lab-result/{orderId}")
    @PreAuthorize("hasAuthority('DIAGNOSTICS_WRITE')")
    @Operation(summary = "Update lab result", description = "Records the results of a completed laboratory test")
    public ResponseEntity<BaseResponse<LabOrder>> updateLabResult(
            @PathVariable Long orderId,
            @RequestParam String result,
            @RequestParam String remarks) {
        LabOrder order = diagnosticsService.updateLabResult(orderId, result, remarks);
        return ResponseEntity.ok(new BaseResponse<>(true, "Lab result updated successfully", null, order));
    }
}
