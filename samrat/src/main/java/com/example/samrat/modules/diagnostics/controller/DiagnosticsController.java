package com.example.samrat.modules.diagnostics.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.diagnostics.lab.entity.LabOrder;
import com.example.samrat.modules.diagnostics.radiology.entity.RadiologyOrder;
import com.example.samrat.modules.diagnostics.service.DiagnosticsService;
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
@RequestMapping("/api/v1/diagnostics")
@RequiredArgsConstructor
public class DiagnosticsController {

    private final DiagnosticsService diagnosticsService;

    // --- V1 - labRoute ---

    @GetMapping("/lab")
    @Tag(name = "V1 - labRoute")
    @Operation(summary = "List V1 - labRoute")
    public ResponseEntity<BaseResponse<Page<LabOrder>>> listLabV1(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ResponseEntity.ok(new BaseResponse<>(true, "Lab list", null, diagnosticsService.searchLabOrders(null, null, null, null, null, pageable)));
    }

    @PostMapping("/lab")
    @Tag(name = "V1 - labRoute")
    @Operation(summary = "Create V1 - labRoute")
    public ResponseEntity<BaseResponse<LabOrder>> createLabV1(
            @RequestBody LabOrder order,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long departmentId) {
        LabOrder created = diagnosticsService.createLabOrder(order, patientId, doctorId, departmentId);
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, created));
    }

    @GetMapping("/lab/{id}")
    @Tag(name = "V1 - labRoute")
    @Operation(summary = "Get V1 - labRoute by ID")
    public ResponseEntity<BaseResponse<LabOrder>> getLabByIdV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Detail", null, diagnosticsService.getLabOrderById(id)));
    }

    @PutMapping("/lab/{id}")
    @Tag(name = "V1 - labRoute")
    @Operation(summary = "Update V1 - labRoute")
    public ResponseEntity<BaseResponse<LabOrder>> updateLabV1(@PathVariable Long id, @RequestBody LabOrder order) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, diagnosticsService.updateLabResult(id, order.getResult(), order.getRemarks())));
    }

    @DeleteMapping("/lab/{id}")
    @Tag(name = "V1 - labRoute")
    @Operation(summary = "Delete V1 - labRoute")
    public ResponseEntity<BaseResponse<Void>> deleteLabV1(@PathVariable Long id) {
        diagnosticsService.deleteLabOrder(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Deleted", null, null));
    }

    // --- V1 - radiologyRoute ---

    @GetMapping("/radiology")
    @Tag(name = "V1 - radiologyRoute")
    @Operation(summary = "List V1 - radiologyRoute")
    public ResponseEntity<BaseResponse<Page<RadiologyOrder>>> listRadiologyV1(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ResponseEntity.ok(new BaseResponse<>(true, "Radiology list", null, diagnosticsService.searchRadiologyOrders(null, null, null, null, null, pageable)));
    }

    @PostMapping("/radiology")
    @Tag(name = "V1 - radiologyRoute")
    @Operation(summary = "Create V1 - radiologyRoute")
    public ResponseEntity<BaseResponse<RadiologyOrder>> createRadiologyV1(
            @RequestBody RadiologyOrder order,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long departmentId) {
        RadiologyOrder created = diagnosticsService.createRadiologyOrder(order, patientId, doctorId, departmentId);
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, created));
    }

    @GetMapping("/radiology/{id}")
    @Tag(name = "V1 - radiologyRoute")
    @Operation(summary = "Get V1 - radiologyRoute by ID")
    public ResponseEntity<BaseResponse<RadiologyOrder>> getRadiologyByIdV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Detail", null, diagnosticsService.getRadiologyOrderById(id)));
    }

    @PutMapping("/radiology/{id}")
    @Tag(name = "V1 - radiologyRoute")
    @Operation(summary = "Update V1 - radiologyRoute")
    public ResponseEntity<BaseResponse<RadiologyOrder>> updateRadiologyV1(@PathVariable Long id, @RequestBody RadiologyOrder order) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, diagnosticsService.updateRadiologyReport(id, order.getReport(), order.getImpression())));
    }

    @DeleteMapping("/radiology/{id}")
    @Tag(name = "V1 - radiologyRoute")
    @Operation(summary = "Delete V1 - radiologyRoute")
    public ResponseEntity<BaseResponse<Void>> deleteRadiologyV1(@PathVariable Long id) {
        diagnosticsService.deleteRadiologyOrder(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Deleted", null, null));
    }

    // --- Lab Management (Existing) ---

    @PostMapping("/lab/orders")
    @PreAuthorize("hasAuthority('DIAGNOSTICS_WRITE')")
    @Operation(summary = "Create lab order", description = "Schedules a new laboratory test for a patient")
    public ResponseEntity<BaseResponse<LabOrder>> createLabOrder(
            @RequestBody LabOrder order,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long departmentId) {
        LabOrder created = diagnosticsService.createLabOrder(order, patientId, doctorId, departmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Lab order created successfully", null, created));
    }

    @GetMapping("/lab/orders/search")
    @PreAuthorize("hasAuthority('DIAGNOSTICS_READ')")
    @Operation(summary = "Search lab orders")
    public ResponseEntity<BaseResponse<Page<LabOrder>>> searchLabOrders(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Lab orders found", null, diagnosticsService.searchLabOrders(patientId, doctorId, status, start, end, pageable)));
    }

    @PatchMapping("/lab/orders/{id}/result")
    @PreAuthorize("hasAuthority('DIAGNOSTICS_WRITE')")
    @Operation(summary = "Update lab result", description = "Records the results of a completed laboratory test")
    public ResponseEntity<BaseResponse<LabOrder>> updateLabResult(
            @PathVariable Long id,
            @RequestParam String result,
            @RequestParam(required = false) String remarks) {
        LabOrder order = diagnosticsService.updateLabResult(id, result, remarks);
        return ResponseEntity.ok(new BaseResponse<>(true, "Lab result updated successfully", null, order));
    }

    // --- Radiology Management ---

    @PostMapping("/radiology/orders")
    @PreAuthorize("hasAuthority('DIAGNOSTICS_WRITE')")
    @Operation(summary = "Create radiology order")
    public ResponseEntity<BaseResponse<RadiologyOrder>> createRadiologyOrder(
            @RequestBody RadiologyOrder order,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long departmentId) {
        RadiologyOrder created = diagnosticsService.createRadiologyOrder(order, patientId, doctorId, departmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Radiology order created successfully", null, created));
    }

    @GetMapping("/radiology/orders/search")
    @PreAuthorize("hasAuthority('DIAGNOSTICS_READ')")
    @Operation(summary = "Search radiology orders")
    public ResponseEntity<BaseResponse<Page<RadiologyOrder>>> searchRadiologyOrders(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Radiology orders found", null, diagnosticsService.searchRadiologyOrders(patientId, doctorId, status, start, end, pageable)));
    }

    @PatchMapping("/radiology/orders/{id}/report")
    @PreAuthorize("hasAuthority('DIAGNOSTICS_WRITE')")
    @Operation(summary = "Update radiology report")
    public ResponseEntity<BaseResponse<RadiologyOrder>> updateRadiologyReport(
            @PathVariable Long id,
            @RequestParam String report,
            @RequestParam(required = false) String impression) {
        RadiologyOrder order = diagnosticsService.updateRadiologyReport(id, report, impression);
        return ResponseEntity.ok(new BaseResponse<>(true, "Radiology report updated successfully", null, order));
    }
}
