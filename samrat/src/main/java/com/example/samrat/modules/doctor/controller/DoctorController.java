package com.example.samrat.modules.doctor.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.doctor.dto.DoctorDTO;
import com.example.samrat.modules.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/available")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    public ResponseEntity<BaseResponse<List<DoctorDTO>>> getAvailableDoctors() {
        List<DoctorDTO> doctors = doctorService.getAvailableDoctors();
        return ResponseEntity.ok(new BaseResponse<>(true, "Available doctors found", null, doctors));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    public ResponseEntity<BaseResponse<List<DoctorDTO>>> getDoctorsBySpecialization(@RequestParam String specialization) {
        List<DoctorDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctors found by specialization", null, doctors));
    }
}
