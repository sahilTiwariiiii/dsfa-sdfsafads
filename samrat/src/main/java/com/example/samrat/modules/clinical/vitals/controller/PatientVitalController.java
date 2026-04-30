package com.example.samrat.modules.clinical.vitals.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.clinical.vitals.dto.PatientVitalDTO;
import com.example.samrat.modules.clinical.vitals.service.PatientVitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clinical/vitals")
@RequiredArgsConstructor
@Tag(name = "V1 - PatientVitals", description = "APIs for tracking historical patient vitals")
public class PatientVitalController {

    private final PatientVitalService patientVitalService;

    @PostMapping
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Record patient vitals", description = "Records a new set of vitals for a patient. Can be called multiple times per visit.")
    public ResponseEntity<BaseResponse<PatientVitalDTO>> recordVitals(@RequestBody PatientVitalDTO dto) {
        PatientVitalDTO saved = patientVitalService.recordVitals(dto);
        return ResponseEntity.ok(new BaseResponse<>(true, "Vitals recorded successfully", null, saved));
    }

    @GetMapping("/history/{patientId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Get vitals history", description = "Retrieves all historical vitals for a patient, ordered by date descending.")
    public ResponseEntity<BaseResponse<List<PatientVitalDTO>>> getVitalsHistory(@PathVariable Long patientId) {
        List<PatientVitalDTO> history = patientVitalService.getPatientVitalsHistory(patientId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Vitals history retrieved", null, history));
    }
}
