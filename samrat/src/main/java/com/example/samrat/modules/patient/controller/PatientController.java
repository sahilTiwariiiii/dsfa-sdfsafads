package com.example.samrat.modules.patient.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.patient.dto.PatientDTO;
import com.example.samrat.modules.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('PATIENT_CREATE')")
    public ResponseEntity<BaseResponse<PatientDTO>> registerPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO registeredPatient = patientService.registerPatient(patientDTO);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient registered successfully", null, registeredPatient));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public ResponseEntity<BaseResponse<List<PatientDTO>>> searchByPhone(@RequestParam String phoneNumber) {
        List<PatientDTO> patients = patientService.getPatientsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patients found", null, patients));
    }

    @GetMapping("/{uhid}")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public ResponseEntity<BaseResponse<PatientDTO>> getPatientByUhid(@PathVariable String uhid) {
        PatientDTO patient = patientService.getPatientByUhid(uhid);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient found", null, patient));
    }

    @GetMapping("/{id}/family")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public ResponseEntity<BaseResponse<List<PatientDTO>>> getFamilyMembers(@PathVariable Long id) {
        List<PatientDTO> family = patientService.getFamilyMembers(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Family members found", null, family));
    }
}
