package com.example.samrat.modules.ipd.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.service.IPDService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ipd")
@RequiredArgsConstructor
@Tag(name = "IPD Management", description = "APIs for managing inpatient admissions and discharges")
public class IPDController {

    private final IPDService ipdService;

    @PostMapping("/admit")
    @PreAuthorize("hasAuthority('IPD_WRITE')")
    @Operation(summary = "Admit patient", description = "Admits a patient to a specific bed under a doctor's care")
    public ResponseEntity<BaseResponse<Admission>> admitPatient(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam Long bedId,
            @RequestParam String reason) {
        Admission admission = ipdService.admitPatient(patientId, doctorId, bedId, reason);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient admitted successfully", null, admission));
    }

    @PostMapping("/discharge/{admissionId}")
    @PreAuthorize("hasAuthority('IPD_WRITE')")
    @Operation(summary = "Discharge patient", description = "Discharges a patient from the hospital and releases the bed")
    public ResponseEntity<BaseResponse<Admission>> dischargePatient(@PathVariable Long admissionId) {
        Admission admission = ipdService.dischargePatient(admissionId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient discharged successfully", null, admission));
    }
}
