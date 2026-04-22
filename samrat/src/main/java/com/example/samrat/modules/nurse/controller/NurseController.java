package com.example.samrat.modules.nurse.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.nurse.dto.NurseDTO;
import com.example.samrat.modules.nurse.service.NurseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
@RequiredArgsConstructor
@Tag(name = "Nurse Management", description = "APIs for nurse search and availability management")
public class NurseController {

    private final NurseService nurseService;

    @GetMapping("/available")
    @PreAuthorize("hasAuthority('NURSE_READ')")
    @Operation(summary = "Get available nurses", description = "Retrieves a list of nurses currently available")
    public ResponseEntity<BaseResponse<List<NurseDTO>>> getAvailableNurses() {
        List<NurseDTO> nurses = nurseService.getAvailableNurses();
        return ResponseEntity.ok(new BaseResponse<>(true, "Available nurses found", null, nurses));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAuthority('NURSE_READ')")
    @Operation(summary = "Get nurses by department", description = "Finds nurses based on their department")
    public ResponseEntity<BaseResponse<List<NurseDTO>>> getNursesByDepartment(@PathVariable Long departmentId) {
        List<NurseDTO> nurses = nurseService.getNursesByDepartment(departmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Nurses found for department", null, nurses));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('NURSE_READ')")
    @Operation(summary = "Get nurse by ID", description = "Retrieves a nurse's details using their ID")
    public ResponseEntity<BaseResponse<NurseDTO>> getNurseById(@PathVariable Long id) {
        NurseDTO nurse = nurseService.getNurseById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Nurse found", null, nurse));
    }
}
