package com.example.samrat.modules.doctor.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.doctor.dto.DoctorDTO;
import com.example.samrat.modules.doctor.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Management", description = "APIs for doctor profile and availability management")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasAuthority('DOCTOR_CREATE')")
    @Operation(summary = "Register new doctor", description = "Creates a new doctor profile")
    public ResponseEntity<BaseResponse<DoctorDTO>> registerDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO registeredDoctor = doctorService.registerDoctor(doctorDTO);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor registered successfully", null, registeredDoctor));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Get all doctors", description = "Retrieves a paginated list of all doctors")
    public ResponseEntity<BaseResponse<Page<DoctorDTO>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorDTO> doctors = doctorService.getAllDoctors(pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctors retrieved successfully", null, doctors));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Get doctor by ID", description = "Retrieves a doctor's details by ID")
    public ResponseEntity<BaseResponse<DoctorDTO>> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor found", null, doctor));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCTOR_UPDATE')")
    @Operation(summary = "Update doctor", description = "Updates an existing doctor profile")
    public ResponseEntity<BaseResponse<DoctorDTO>> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO updatedDoctor = doctorService.updateDoctor(id, doctorDTO);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor updated successfully", null, updatedDoctor));
    }

    @GetMapping("/available")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Get available doctors", description = "Retrieves a list of doctors currently available for consultations")
    public ResponseEntity<BaseResponse<List<DoctorDTO>>> getAvailableDoctors() {
        List<DoctorDTO> doctors = doctorService.getAvailableDoctors();
        return ResponseEntity.ok(new BaseResponse<>(true, "Available doctors found", null, doctors));
    }

    @GetMapping("/specialization")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Get doctors by specialization", description = "Finds doctors based on their medical specialization")
    public ResponseEntity<BaseResponse<List<DoctorDTO>>> getDoctorsBySpecialization(@RequestParam String specialization) {
        List<DoctorDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctors found by specialization", null, doctors));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Get doctors by department", description = "Retrieves all doctors belonging to a specific department")
    public ResponseEntity<BaseResponse<List<DoctorDTO>>> getDoctorsByDepartment(@PathVariable Long departmentId) {
        List<DoctorDTO> doctors = doctorService.getDoctorsByDepartment(departmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctors found for department", null, doctors));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @Operation(summary = "Search doctors", description = "Search doctors by name or specialization with optional department filter")
    public ResponseEntity<BaseResponse<Page<DoctorDTO>>> searchDoctors(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorDTO> doctors = doctorService.searchDoctors(departmentId, query, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Search results found", null, doctors));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCTOR_DELETE')")
    @Operation(summary = "Delete doctor", description = "Deletes a doctor record")
    public ResponseEntity<BaseResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor deleted successfully", null, null));
    }
}
