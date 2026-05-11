package com.example.samrat.modules.ipd.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.ipd.dto.AdmissionRequest;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.entity.Bed;
import com.example.samrat.modules.ipd.entity.Ward;
import com.example.samrat.modules.ipd.service.IPDService;
import jakarta.validation.Valid;
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
    @PreAuthorize("hasAuthority('IPD_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a paginated list of all IPD admissions")
    public ResponseEntity<BaseResponse<Page<Admission>>> listIpdV1(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "IPD list", null, ipdService.searchAdmissions(null, null, null, null, null, null, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('IPD_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Admission>> getIpdByIdV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Detail", null, ipdService.getAdmissionById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('IPD_WRITE')")
    @Operation(summary = "Update admission")
    public ResponseEntity<BaseResponse<Admission>> updateIpdV1(@PathVariable Long id, @RequestBody Admission admission) {
        // Implementation for update would go here
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, admission));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('IPD_DELETE')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Void>> deleteIpdV1(@PathVariable Long id) {
        ipdService.deleteAdmission(id);
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
    @Operation(summary = "Method Summary")
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
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Page<Bed>>> getAllBeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Beds found", null, ipdService.getAllBeds(pageable)));
    }

    @GetMapping("/beds/available")
    @PreAuthorize("hasAuthority('IPD_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<List<Bed>>> getAvailableBeds(@RequestParam Long wardId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Available beds found", null, ipdService.getAvailableBedsByWard(wardId)));
    }

    @GetMapping("/beds/{bedId}")
    @PreAuthorize("hasAuthority('IPD_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Bed>> getBedById(@PathVariable Long bedId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Bed found", null, ipdService.getBedById(bedId)));
    }

    // --- Admission Management ---

    @PostMapping("/admit")
    @PreAuthorize("hasAuthority('IPD_WRITE')")
    @Operation(summary = "Admit patient", description = "Admits a patient to a specific bed under a doctor's care")
    public ResponseEntity<BaseResponse<Admission>> admitPatient(
            @Valid @RequestBody AdmissionRequest request) {
        Admission created = ipdService.admitPatient(request);
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

