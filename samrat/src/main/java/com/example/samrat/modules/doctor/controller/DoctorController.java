package com.example.samrat.modules.doctor.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.doctor.dto.DoctorDTO;
import com.example.samrat.modules.doctor.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Management", description = "APIs for doctor search and availability management")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/available")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Get available doctors", description = "Retrieves a list of doctors currently available for consultations")
    public ResponseEntity<BaseResponse<List<DoctorDTO>>> getAvailableDoctors() {
        List<DoctorDTO> doctors = doctorService.getAvailableDoctors();
        return ResponseEntity.ok(new BaseResponse<>(true, "Available doctors found", null, doctors));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Search doctors by specialization", description = "Finds doctors based on their medical specialization")
    public ResponseEntity<BaseResponse<List<DoctorDTO>>> getDoctorsBySpecialization(@RequestParam String specialization) {
        List<DoctorDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctors found by specialization", null, doctors));
    }
}
