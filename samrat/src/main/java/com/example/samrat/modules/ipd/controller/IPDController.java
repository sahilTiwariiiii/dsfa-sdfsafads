package com.example.samrat.modules.ipd.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.entity.Bed;
import com.example.samrat.modules.ipd.entity.Ward;
import com.example.samrat.modules.ipd.service.IPDService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/ipd")
@RequiredArgsConstructor
@Tag(name = "V1 - ipdRoute", description = "Enterprise APIs for managing wards, beds, admissions, and discharges")
public class IPDController {

    private final IPDService ipdService;

    @GetMapping
    @Operation(summary = "List V1 - ipdRoute")
    public ResponseEntity<BaseResponse<Page<Admission>>> listIpdV1(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ResponseEntity.ok(new BaseResponse<>(true, "IPD list", null, ipdService.searchAdmissions(null, null, null, null, null, null, pageable)));
    }

    @PostMapping
    @Operation(summary = "Create V1 - ipdRoute")
    public ResponseEntity<BaseResponse<Admission>> createIpdV1(@RequestBody Admission admission) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, admission));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get V1 - ipdRoute by ID")
    public ResponseEntity<BaseResponse<Admission>> getIpdByIdV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Detail", null, null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update V1 - ipdRoute")
    public ResponseEntity<BaseResponse<Admission>> updateIpdV1(@PathVariable Long id, @RequestBody Admission admission) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, admission));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete V1 - ipdRoute")
    public ResponseEntity<BaseResponse<Void>> deleteIpdV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Deleted", null, null));
    }

    // --- Ward Management ---

    @PostMapping("/wards")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    @Operation(summary = "Create ward")
    public ResponseEntity<BaseResponse<Ward>> createWard(@RequestBody Ward ward, @RequestParam(required = false) Long departmentId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Ward created", null, ipdService.createWard(ward, departmentId)));
    }

    @GetMapping("/wards")
    @PreAuthorize("hasAuthority('IPD_READ')")
    @Operation(summary = "Get all wards")
    public ResponseEntity<BaseResponse<Page<Ward>>> getAllWards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Wards found", null, ipdService.getAllWards(pageable)));
    }

    // --- Bed Management ---

    @PostMapping("/beds")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    @Operation(summary = "Create bed")
    public ResponseEntity<BaseResponse<Bed>> createBed(@RequestBody Bed bed, @RequestParam Long wardId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Bed created", null, ipdService.createBed(bed, wardId)));
    }

    @GetMapping("/beds")
    @PreAuthorize("hasAuthority('IPD_READ')")
    @Operation(summary = "Get all beds")
    public ResponseEntity<BaseResponse<Page<Bed>>> getAllBeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Beds found", null, ipdService.getAllBeds(pageable)));
    }

    @GetMapping("/beds/available")
    @PreAuthorize("hasAuthority('IPD_READ')")
    @Operation(summary = "Get available beds by ward")
    public ResponseEntity<BaseResponse<List<Bed>>> getAvailableBeds(@RequestParam Long wardId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Available beds found", null, ipdService.getAvailableBedsByWard(wardId)));
    }

    // --- Admission Management ---

    @PostMapping("/admit")
    @PreAuthorize("hasAuthority('IPD_WRITE')")
    @Operation(summary = "Admit patient", description = "Admits a patient to a specific bed under a doctor's care")
    public ResponseEntity<BaseResponse<Admission>> admitPatient(
            @RequestBody Admission admission,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam Long bedId,
            @RequestParam(required = false) Long departmentId) {
        Admission created = ipdService.admitPatient(admission, patientId, doctorId, bedId, departmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient admitted successfully", null, created));
    }

    @GetMapping("/admissions/search")
    @PreAuthorize("hasAuthority('IPD_READ')")
    @Operation(summary = "Search admissions")
    public ResponseEntity<BaseResponse<Page<Admission>>> searchAdmissions(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Admissions found", null, ipdService.searchAdmissions(patientId, doctorId, departmentId, status, start, end, pageable)));
    }

    @PostMapping("/discharge/{admissionId}")
    @PreAuthorize("hasAuthority('IPD_WRITE')")
    @Operation(summary = "Discharge patient", description = "Discharges a patient from the hospital and releases the bed")
    public ResponseEntity<BaseResponse<Admission>> dischargePatient(@PathVariable Long admissionId) {
        Admission admission = ipdService.dischargePatient(admissionId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient discharged successfully", null, admission));
    }

    @PostMapping("/transfer-bed/{admissionId}")
    @PreAuthorize("hasAuthority('IPD_WRITE')")
    @Operation(summary = "Transfer patient bed")
    public ResponseEntity<BaseResponse<Admission>> transferBed(
            @PathVariable Long admissionId,
            @RequestParam Long newBedId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Bed transferred successfully", null, ipdService.transferBed(admissionId, newBedId)));
    }
}
