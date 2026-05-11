package com.example.samrat.modules.patient.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.patient.dto.PatientDTO;
import com.example.samrat.modules.patient.dto.PatientRegistrationRequest;
import com.example.samrat.modules.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "V1 - PatientRegistrationAndVisitedRoute", description = "Enterprise APIs for patient registration and visits")
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/patient-register")
    @PreAuthorize("hasAuthority('PATIENT_CREATE')")
    @Operation(
            summary = "Method Summary",
            description = "Registers a new patient and records their initial visit details",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Patient registered and visited successfully",
                            content = @Content(schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation or tenant context error",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "ValidationError",
                                                    value = "{\"success\":false,\"message\":\"Validation Failed\",\"error\":null,\"data\":{\"mobile\":\"Mobile number is required\"}}"
                                            ),
                                            @ExampleObject(
                                                    name = "TenantError",
                                                    value = "{\"success\":false,\"message\":\"Hospital/Branch context is missing. Please login again.\",\"error\":null,\"data\":null}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - missing or invalid JWT token",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "Unauthorized",
                                            value = "{\"success\":false,\"message\":\"Unauthorized\",\"error\":\"Full authentication is required to access this resource\",\"data\":null}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<BaseResponse<PatientDTO>> patientRegisterV1(@Valid @RequestBody PatientRegistrationRequest registrationData) {
        PatientDTO registeredPatient = patientService.registerPatientFromRequest(registrationData);
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "User registered and Visited Successfully", null, registeredPatient));
    }

    // --- Existing Patient Management APIs ---

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('PATIENT_CREATE')")
    @Operation(
            summary = "Register new patient",
            description = "Creates a new patient profile in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patient registered successfully",
                            content = @Content(schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error - Check request body for missing required fields",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "ValidationError",
                                            value = "{\"success\":false,\"message\":\"Validation Failed\",\"error\":null,\"data\":{\"firstName\":\"must not be blank\"}}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - missing or invalid JWT token",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "Unauthorized",
                                            value = "{\"success\":false,\"message\":\"Unauthorized\",\"error\":\"Full authentication is required to access this resource\",\"data\":null}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - user lacks PATIENT_CREATE permission",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "Forbidden",
                                            value = "{\"success\":false,\"message\":\"Access denied\",\"error\":\"Access is denied\",\"data\":null}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<BaseResponse<PatientDTO>> registerPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO registeredPatient = patientService.registerPatient(patientDTO);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient registered successfully", null, registeredPatient));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a paginated list of all patients")
    public ResponseEntity<BaseResponse<Page<PatientDTO>>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort[0])));
        Page<PatientDTO> patients = patientService.getAllPatients(pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patients retrieved successfully", null, patients));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a patient's details using their internal database ID")
    public ResponseEntity<BaseResponse<PatientDTO>> getPatientById(@PathVariable Long id) {
        PatientDTO patient = patientService.getPatientById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient found", null, patient));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_UPDATE')")
    @Operation(summary = "Update patient", description = "Updates an existing patient profile")
    public ResponseEntity<BaseResponse<PatientDTO>> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient updated successfully", null, updatedPatient));
    }

    @GetMapping("/search-by-phone")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    @Operation(summary = "Search patient by phone", description = "Finds patients based on their phone number")
    public ResponseEntity<BaseResponse<List<PatientDTO>>> searchByPhone(@RequestParam String phoneNumber) {
        List<PatientDTO> patients = patientService.getPatientsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patients found", null, patients));
    }

    @GetMapping("/uhid/{uhid}")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a patient's details using their unique hospital ID")
    public ResponseEntity<BaseResponse<PatientDTO>> getPatientByUhid(@PathVariable String uhid) {
        PatientDTO patient = patientService.getPatientByUhid(uhid);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient found", null, patient));
    }

    @GetMapping("/{id}/family")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a list of family members for a specific patient")
    public ResponseEntity<BaseResponse<List<PatientDTO>>> getFamilyMembers(@PathVariable Long id) {
        List<PatientDTO> family = patientService.getFamilyMembers(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Family members found", null, family));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    @Operation(summary = "Search patients", description = "Search patients by name, phone, or UHID with pagination")
    public ResponseEntity<BaseResponse<Page<PatientDTO>>> searchPatients(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientDTO> patients = patientService.searchPatients(query, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Search results found", null, patients));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_DELETE')")
    @Operation(summary = "Method Summary", description = "Deletes a patient record")
    public ResponseEntity<BaseResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Patient deleted successfully", null, null));
    }
}

