package com.example.samrat.modules.clinical.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.clinical.emr.entity.EMRRecord;
import com.example.samrat.modules.clinical.nursing.entity.NursingNote;
import com.example.samrat.modules.clinical.service.ClinicalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinical")
@RequiredArgsConstructor
@Tag(name = "Clinical Management", description = "APIs for EMR, Nursing notes, and clinical records")
public class ClinicalController {

    private final ClinicalService clinicalService;

    @PostMapping("/emr")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Create EMR record", description = "Records a new electronic medical record for a patient")
    public ResponseEntity<BaseResponse<EMRRecord>> createEMR(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam String complaint,
            @RequestParam String diagnosis,
            @RequestParam String prescription) {
        EMRRecord record = clinicalService.createEMRRecord(patientId, doctorId, complaint, diagnosis, prescription);
        return ResponseEntity.ok(new BaseResponse<>(true, "EMR record created successfully", null, record));
    }

    @PostMapping("/nursing-note")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Add nursing note", description = "Adds a nursing care note for an admitted patient")
    public ResponseEntity<BaseResponse<NursingNote>> addNursingNote(
            @RequestParam Long admissionId,
            @RequestParam String description,
            @RequestParam Double temp,
            @RequestParam Double pulse,
            @RequestParam String status) {
        NursingNote note = clinicalService.addNursingNote(admissionId, description, temp, pulse, status);
        return ResponseEntity.ok(new BaseResponse<>(true, "Nursing note added successfully", null, note));
    }
}
