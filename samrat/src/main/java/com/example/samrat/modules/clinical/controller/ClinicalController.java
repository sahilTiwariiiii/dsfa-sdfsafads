package com.example.samrat.modules.clinical.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.clinical.dto.CreateClinicalDetailsRequest;
import com.example.samrat.modules.clinical.dto.EMRRecordResponseDTO;
import com.example.samrat.modules.clinical.discharge.entity.DischargeSummary;
import com.example.samrat.modules.clinical.emergency.entity.ERVisit;
import com.example.samrat.modules.clinical.emr.entity.*;
import com.example.samrat.modules.clinical.nursing.entity.NursingNote;
import com.example.samrat.modules.clinical.ot.entity.OTBooking;
import com.example.samrat.modules.clinical.service.ClinicalService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/clinical")
@RequiredArgsConstructor
@Tag(name = "V1 - clinicalDetailsRoute", description = "Enterprise APIs for EMR, Histories, Diagnoses, Notes, Prescriptions, Nursing, Discharge, Emergency, and OT")
public class ClinicalController {

    private final ClinicalService clinicalService;

    // --- Core Clinical Details (v1) ---

    @GetMapping
    @Operation(
            summary = "List V1 - clinicalDetailsRoute",
            description = "Retrieves a paginated list of clinical records with optional filters for room, status, doctor, and date",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Clinical details retrieved successfully"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
            }
    )
    public ResponseEntity<BaseResponse<Page<EMRRecordResponseDTO>>> listClinicalDetails(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<EMRRecordResponseDTO> records = clinicalService
                .searchEMRRecords(null, doctorId, null, status, null, pageable)
                .map(this::toEMRResponseDTO);
        return ResponseEntity.ok(new BaseResponse<>(true, "Clinical details list", null, records));
    }

    @PostMapping
    @Operation(
            summary = "Create V1 - clinicalDetailsRoute",
            description = "Creates a clinical record.\n\n" +
                    "Required fields in request body:\n" +
                    "- patientId\n" +
                    "- doctorId\n\n" +
                    "All other request body fields are optional.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Clinical record request body.\nRequired: patientId, doctorId.\nOptional: all other fields.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateClinicalDetailsRequest.class),
                            examples = @ExampleObject(
                                    name = "CreateClinicalDetailsRequestExample",
                                    value = "{\n" +
                                            "  \"patientId\": 101,\n" +
                                            "  \"doctorId\": 21,\n" +
                                            "  \"departmentId\": 5,\n" +
                                            "  \"chiefComplaint\": \"Fever with cough for 3 days\",\n" +
                                            "  \"bloodPressure\": \"120/80\",\n" +
                                            "  \"bodyTemperature\": 98.6,\n" +
                                            "  \"heartRate\": 78,\n" +
                                            "  \"respiratoryRate\": 16,\n" +
                                            "  \"oxygenSaturation\": 98,\n" +
                                            "  \"weight\": 72.4,\n" +
                                            "  \"height\": 172,\n" +
                                            "  \"bmi\": 24.5,\n" +
                                            "  \"historyOfPresentIllness\": \"Intermittent fever, mild breathlessness\",\n" +
                                            "  \"pastMedicalHistory\": \"Type 2 diabetes for 5 years\",\n" +
                                            "  \"allergies\": \"Penicillin\",\n" +
                                            "  \"physicalExamination\": \"Mild wheeze on auscultation\",\n" +
                                            "  \"diagnosis\": \"Acute bronchitis\",\n" +
                                            "  \"prescription\": \"Paracetamol 650 mg SOS\",\n" +
                                            "  \"labOrders\": \"CBC, CRP\",\n" +
                                            "  \"radiologyOrders\": \"Chest X-ray PA view\",\n" +
                                            "  \"status\": \"ACTIVE\"\n" +
                                            "}"
                            )
                    )
            )
    )
    public ResponseEntity<BaseResponse<EMRRecordResponseDTO>> createClinicalDetails(
            @Valid
            @org.springframework.web.bind.annotation.RequestBody
            CreateClinicalDetailsRequest request) {
        EMRRecord record = new EMRRecord();
        record.setChiefComplaint(request.getChiefComplaint());
        record.setBloodPressure(request.getBloodPressure());
        record.setBodyTemperature(request.getBodyTemperature());
        record.setHeartRate(request.getHeartRate());
        record.setRespiratoryRate(request.getRespiratoryRate());
        record.setOxygenSaturation(request.getOxygenSaturation());
        record.setWeight(request.getWeight());
        record.setHeight(request.getHeight());
        record.setBmi(request.getBmi());
        record.setHistoryOfPresentIllness(request.getHistoryOfPresentIllness());
        record.setPastMedicalHistory(request.getPastMedicalHistory());
        record.setAllergies(request.getAllergies());
        record.setPhysicalExamination(request.getPhysicalExamination());
        record.setDiagnosis(request.getDiagnosis());
        record.setPrescription(request.getPrescription());
        record.setLabOrders(request.getLabOrders());
        record.setRadiologyOrders(request.getRadiologyOrders());
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            record.setStatus(request.getStatus());
        }

        EMRRecord createdRecord = clinicalService.createEMRRecord(record, request.getPatientId(), request.getDoctorId(), request.getDepartmentId());

        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Created",
                null,
                toEMRResponseDTO(createdRecord)
        ));
    }

    private EMRRecordResponseDTO toEMRResponseDTO(EMRRecord record) {
        EMRRecordResponseDTO dto = new EMRRecordResponseDTO();
        dto.setId(record.getId());
        if (record.getPatient() != null) {
            dto.setPatientId(record.getPatient().getId());
        }
        if (record.getDoctor() != null) {
            dto.setDoctorId(record.getDoctor().getId());
        }
        if (record.getDepartment() != null) {
            dto.setDepartmentId(record.getDepartment().getId());
        }

        dto.setChiefComplaint(record.getChiefComplaint());
        dto.setBloodPressure(record.getBloodPressure());
        dto.setBodyTemperature(record.getBodyTemperature());
        dto.setHeartRate(record.getHeartRate());
        dto.setRespiratoryRate(record.getRespiratoryRate());
        dto.setOxygenSaturation(record.getOxygenSaturation());
        dto.setWeight(record.getWeight());
        dto.setHeight(record.getHeight());
        dto.setBmi(record.getBmi());
        dto.setHistoryOfPresentIllness(record.getHistoryOfPresentIllness());
        dto.setPastMedicalHistory(record.getPastMedicalHistory());
        dto.setAllergies(record.getAllergies());
        dto.setPhysicalExamination(record.getPhysicalExamination());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setPrescription(record.getPrescription());
        dto.setLabOrders(record.getLabOrders());
        dto.setRadiologyOrders(record.getRadiologyOrders());
        dto.setStatus(record.getStatus());
        return dto;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<EMRRecordResponseDTO>> getClinicalDetailById(@PathVariable Long id) {
        EMRRecord record = clinicalService.getEMRRecordById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Detail", null, toEMRResponseDTO(record)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update V1 - clinicalDetailsRoute")
    public ResponseEntity<BaseResponse<EMRRecordResponseDTO>> updateClinicalDetail(@PathVariable Long id, @RequestBody EMRRecord record) {
        EMRRecord updated = clinicalService.updateEMRRecord(id, record);
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, toEMRResponseDTO(updated)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Void>> deleteClinicalDetail(@PathVariable Long id) {
        // Implementation for delete would go here
        return ResponseEntity.ok(new BaseResponse<>(true, "Deleted", null, null));
    }

    // --- Addiction / Personal History (v1) ---

    @PostMapping("/addiction")
    @Operation(summary = "addiction")
    public ResponseEntity<BaseResponse<PersonalHistory>> addAddiction(@RequestBody PersonalHistory history) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Addiction record added", null, clinicalService.addPersonalHistory(history, null)));
    }

    @GetMapping("/addiction/{patientId}")
    @Operation(summary = "addiction/:patientId")
    public ResponseEntity<BaseResponse<Page<PersonalHistory>>> getAddictionByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Addiction records found", null, clinicalService.getPersonalHistoryByPatient(patientId, PageRequest.of(0, 10))));
    }

    @PutMapping("/addiction/{id}")
    @Operation(summary = "addiction/:id")
    public ResponseEntity<BaseResponse<PersonalHistory>> updateAddiction(@PathVariable Long id, @RequestBody PersonalHistory history) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Addiction record updated", null, clinicalService.addPersonalHistory(history, null)));
    }

    @DeleteMapping("/addiction/{id}")
    @Operation(summary = "addiction/:id")
    public ResponseEntity<BaseResponse<Void>> deleteAddiction(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Addiction record deleted", null, null));
    }

    @GetMapping("/all-clinical-details/")
    @Operation(summary = "all-clinical-details/")
    public ResponseEntity<BaseResponse<Void>> getAllClinicalDetails() {
        return ResponseEntity.ok(new BaseResponse<>(true, "All clinical details", null, null));
    }

    // --- Surgical History (v1) ---

    @PostMapping("/surgical")
    @Operation(summary = "surgical")
    public ResponseEntity<BaseResponse<SurgicalHistory>> addSurgicalV1(@RequestBody SurgicalHistory history) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Surgical record added", null, clinicalService.addSurgicalHistory(history, null)));
    }

    @GetMapping("/surgical/{patientId}")
    @Operation(summary = "surgical/:patientId")
    public ResponseEntity<BaseResponse<Page<SurgicalHistory>>> getSurgicalByPatientV1(@PathVariable Long patientId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Surgical records found", null, clinicalService.getSurgicalHistoryByPatient(patientId, PageRequest.of(0, 10))));
    }

    @PutMapping("/surgical/{id}")
    @Operation(summary = "surgical/:id")
    public ResponseEntity<BaseResponse<SurgicalHistory>> updateSurgicalV1(@PathVariable Long id, @RequestBody SurgicalHistory history) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Surgical record updated", null, clinicalService.addSurgicalHistory(history, null)));
    }

    // --- Medical History (v1) ---

    @PostMapping("/medical")
    @Operation(summary = "medical")
    public ResponseEntity<BaseResponse<MedicalHistory>> addMedicalV1(@RequestBody MedicalHistory history) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Medical record added", null, clinicalService.addMedicalHistory(history, null)));
    }

    @GetMapping("/medical/{patientId}")
    @Operation(summary = "medical/:patientId")
    public ResponseEntity<BaseResponse<Page<MedicalHistory>>> getMedicalByPatientV1(@PathVariable Long patientId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Medical records found", null, clinicalService.getMedicalHistoryByPatient(patientId, PageRequest.of(0, 10))));
    }

    @PutMapping("/medical/{id}")
    @Operation(summary = "medical/:id")
    public ResponseEntity<BaseResponse<MedicalHistory>> updateMedicalV1(@PathVariable Long id, @RequestBody MedicalHistory history) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Medical record updated", null, clinicalService.addMedicalHistory(history, null)));
    }

    @DeleteMapping("/medical/{id}")
    @Operation(summary = "medical/:id")
    public ResponseEntity<BaseResponse<Void>> deleteMedicalV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Medical record deleted", null, null));
    }

    // --- Personal History (v1) ---

    @PostMapping("/personalhistory")
    @Operation(summary = "personalhistory")
    public ResponseEntity<BaseResponse<PersonalHistory>> addPersonalHistoryV1(@RequestBody PersonalHistory history) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Personal history added", null, clinicalService.addPersonalHistory(history, null)));
    }

    @GetMapping("/personalhistory/{patientId}")
    @Operation(summary = "personalhistory/:patientId")
    public ResponseEntity<BaseResponse<Page<PersonalHistory>>> getPersonalHistoryByPatientV1(@PathVariable Long patientId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Personal history records found", null, clinicalService.getPersonalHistoryByPatient(patientId, PageRequest.of(0, 10))));
    }

    @PutMapping("/personalhistory/{id}")
    @Operation(summary = "personalhistory/:id")
    public ResponseEntity<BaseResponse<PersonalHistory>> updatePersonalHistoryV1(@PathVariable Long id, @RequestBody PersonalHistory history) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Personal history updated", null, clinicalService.addPersonalHistory(history, null)));
    }

    // --- Diagnosis (v1) ---

    @PostMapping("/creatediagnosis")
    @Operation(summary = "creatediagnosis")
    public ResponseEntity<BaseResponse<ClinicalDiagnosis>> createDiagnosisV1(@RequestBody ClinicalDiagnosis diagnosis) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Diagnosis created", null, clinicalService.addDiagnosis(diagnosis, null, null, null)));
    }

    @PutMapping("/updatediagnosis/{id}")
    @Operation(summary = "updatediagnosis/:id")
    public ResponseEntity<BaseResponse<ClinicalDiagnosis>> updateDiagnosisV1(@PathVariable Long id, @RequestBody ClinicalDiagnosis diagnosis) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Diagnosis updated", null, clinicalService.addDiagnosis(diagnosis, null, null, null)));
    }

    @GetMapping("/getdiagnosis/{id}")
    @Operation(summary = "getdiagnosis/:id")
    public ResponseEntity<BaseResponse<ClinicalDiagnosis>> getDiagnosisV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Diagnosis found", null, null));
    }

    @GetMapping("/diagnosispatient/{patientId}")
    @Operation(summary = "diagnosispatient/:patientId")
    public ResponseEntity<BaseResponse<Page<ClinicalDiagnosis>>> getDiagnosisByPatientV1(@PathVariable Long patientId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Diagnoses found", null, clinicalService.getDiagnosesByPatient(patientId, PageRequest.of(0, 10))));
    }

    @GetMapping("/visitdiagnosis/{visitId}")
    @Operation(summary = "visitdiagnosis/:visitId")
    public ResponseEntity<BaseResponse<Void>> getVisitDiagnosisV1(@PathVariable Long visitId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Visit diagnosis found", null, null));
    }

    // --- Doctor Notes (v1) ---

    @PostMapping("/createdoctornotes")
    @Operation(summary = "createdoctornotes")
    public ResponseEntity<BaseResponse<DoctorNote>> createDoctorNoteV1(@RequestBody DoctorNote note) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor note created", null, clinicalService.addDoctorNote(note, null, null)));
    }

    @PutMapping("/updatedoctornotes/{id}")
    @Operation(summary = "updatedoctornotes/:id")
    public ResponseEntity<BaseResponse<DoctorNote>> updateDoctorNoteV1(@PathVariable Long id, @RequestBody DoctorNote note) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor note updated", null, clinicalService.addDoctorNote(note, null, null)));
    }

    @GetMapping("/updatedoctornotes/{id}")
    @Operation(summary = "updatedoctornotes/:id")
    public ResponseEntity<BaseResponse<DoctorNote>> getDoctorNoteV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor note found", null, null));
    }

    @DeleteMapping("/deletedoctornotes/{id}")
    @Operation(summary = "deletedoctornotes/:id")
    public ResponseEntity<BaseResponse<Void>> deleteDoctorNoteV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor note deleted", null, null));
    }

    // --- Prescription (v1) ---

    @PostMapping("/createPrescription/{visitId}")
    @Operation(summary = "createPrescription/:visitId")
    public ResponseEntity<BaseResponse<ClinicalPrescription>> createPrescriptionV1(@PathVariable Long visitId, @RequestBody ClinicalPrescription prescription) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Prescription created", null, clinicalService.addPrescription(prescription, null, null, visitId)));
    }

    @PutMapping("/updatePrescription/{id}")
    @Operation(summary = "updatePrescription/:id")
    public ResponseEntity<BaseResponse<ClinicalPrescription>> updatePrescriptionV1(@PathVariable Long id, @RequestBody ClinicalPrescription prescription) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Prescription updated", null, clinicalService.addPrescription(prescription, null, null, null)));
    }

    @GetMapping("/getPrescription/{id}")
    @Operation(summary = "getPrescription/:id")
    public ResponseEntity<BaseResponse<ClinicalPrescription>> getPrescriptionV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Prescription found", null, null));
    }

    @DeleteMapping("/deletePrescription/{id}")
    @Operation(summary = "deletePrescription/:id")
    public ResponseEntity<BaseResponse<Void>> deletePrescriptionV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Prescription deleted", null, null));
    }

    // --- Existing Enterprise Clinical APIs ---

    // --- EMR Records ---

    @PostMapping("/emr")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Create EMR record", description = "Records a new electronic medical record for a patient")
    public ResponseEntity<BaseResponse<EMRRecordResponseDTO>> createEMR(
            @RequestBody EMRRecord record,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long departmentId) {
        EMRRecord created = clinicalService.createEMRRecord(record, patientId, doctorId, departmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "EMR record created successfully", null, toEMRResponseDTO(created)));
    }

    @GetMapping("/emr/search")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Search EMR records", description = "Filters EMR records by patient, doctor, department, and keywords")
    public ResponseEntity<BaseResponse<Page<EMRRecordResponseDTO>>> searchEMR(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EMRRecordResponseDTO> records = clinicalService.searchEMRRecords(patientId, doctorId, departmentId, status, keyword, pageable)
                .map(this::toEMRResponseDTO);
        return ResponseEntity.ok(new BaseResponse<>(true, "EMR records found", null, records));
    }

    @GetMapping("/emr/{id}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<EMRRecordResponseDTO>> getEMRById(@PathVariable Long id) {
        EMRRecord record = clinicalService.getEMRRecordById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "EMR record found", null, toEMRResponseDTO(record)));
    }

    @PutMapping("/emr/{id}")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Update EMR record")
    public ResponseEntity<BaseResponse<EMRRecordResponseDTO>> updateEMR(@PathVariable Long id, @RequestBody EMRRecord record) {
        EMRRecord updated = clinicalService.updateEMRRecord(id, record);
        return ResponseEntity.ok(new BaseResponse<>(true, "EMR record updated", null, toEMRResponseDTO(updated)));
    }

    // --- Nursing Notes ---

    @PostMapping("/nursing-note")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Add nursing note", description = "Adds a nursing care note for an admitted patient")
    public ResponseEntity<BaseResponse<NursingNote>> addNursingNote(
            @RequestBody NursingNote note,
            @RequestParam Long admissionId) {
        NursingNote created = clinicalService.addNursingNote(note, admissionId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Nursing note added successfully", null, created));
    }

    @GetMapping("/nursing-note/admission/{admissionId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Page<NursingNote>>> getNursingNotes(
            @PathVariable Long admissionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Nursing notes found", null, clinicalService.getNursingNotesByAdmission(admissionId, pageable)));
    }

    // --- Discharge Summary ---

    @PostMapping("/discharge-summary")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Create discharge summary", description = "Creates a discharge summary and updates admission status")
    public ResponseEntity<BaseResponse<DischargeSummary>> createDischargeSummary(
            @RequestBody DischargeSummary summary,
            @RequestParam Long admissionId) {
        DischargeSummary created = clinicalService.createDischargeSummary(summary, admissionId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Discharge summary created successfully", null, created));
    }

    @GetMapping("/discharge-summary/admission/{admissionId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<DischargeSummary>> getDischargeSummary(@PathVariable Long admissionId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Discharge summary found", null, clinicalService.getDischargeSummaryByAdmission(admissionId)));
    }

    // --- Emergency (ER) ---

    @PostMapping("/er/visit")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Register ER visit", description = "Registers a new emergency room visit")
    public ResponseEntity<BaseResponse<ERVisit>> registerERVisit(
            @RequestBody ERVisit visit,
            @RequestParam Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId) {
        ERVisit created = clinicalService.registerERVisit(visit, patientId, doctorId, departmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "ER visit registered", null, created));
    }

    @GetMapping("/er/search")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Search ER visits")
    public ResponseEntity<BaseResponse<Page<ERVisit>>> searchER(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String triage,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "ER visits found", null, clinicalService.searchERVisits(patientId, doctorId, triage, status, start, end, pageable)));
    }

    // --- OT Booking ---

    @PostMapping("/ot/booking")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Schedule OT procedure")
    public ResponseEntity<BaseResponse<OTBooking>> scheduleOT(
            @RequestBody OTBooking booking,
            @RequestParam Long patientId,
            @RequestParam Long surgeonId,
            @RequestParam(required = false) Long anesthetistId,
            @RequestParam(required = false) Long departmentId) {
        OTBooking created = clinicalService.scheduleOT(booking, patientId, surgeonId, anesthetistId, departmentId);
        return ResponseEntity.ok(new BaseResponse<>(true, "OT procedure scheduled", null, created));
    }

    @GetMapping("/ot/search")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Search OT bookings")
    public ResponseEntity<BaseResponse<Page<OTBooking>>> searchOT(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long surgeonId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "OT bookings found", null, clinicalService.searchOTBookings(patientId, surgeonId, departmentId, status, start, end, pageable)));
    }

    // --- Surgical History ---
    @PostMapping("/surgical-history")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Add surgical history")
    public ResponseEntity<BaseResponse<SurgicalHistory>> addSurgicalHistory(
            @RequestBody SurgicalHistory history, @RequestParam Long patientId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Surgical history added", null, clinicalService.addSurgicalHistory(history, patientId)));
    }

    @GetMapping("/surgical-history/patient/{patientId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Page<SurgicalHistory>>> getSurgicalHistory(
            @PathVariable Long patientId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Surgical history found", null, clinicalService.getSurgicalHistoryByPatient(patientId, PageRequest.of(page, size))));
    }

    // --- Medical History ---
    @PostMapping("/medical-history")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Add medical history")
    public ResponseEntity<BaseResponse<MedicalHistory>> addMedicalHistory(
            @RequestBody MedicalHistory history, @RequestParam Long patientId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Medical history added", null, clinicalService.addMedicalHistory(history, patientId)));
    }

    @GetMapping("/medical-history/patient/{patientId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Page<MedicalHistory>>> getMedicalHistory(
            @PathVariable Long patientId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Medical history found", null, clinicalService.getMedicalHistoryByPatient(patientId, PageRequest.of(page, size))));
    }

    // --- Personal History ---
    @PostMapping("/personal-history")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Add personal history")
    public ResponseEntity<BaseResponse<PersonalHistory>> addPersonalHistory(
            @RequestBody PersonalHistory history, @RequestParam Long patientId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Personal history added", null, clinicalService.addPersonalHistory(history, patientId)));
    }

    @GetMapping("/personal-history/patient/{patientId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Page<PersonalHistory>>> getPersonalHistory(
            @PathVariable Long patientId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Personal history found", null, clinicalService.getPersonalHistoryByPatient(patientId, PageRequest.of(page, size))));
    }

    // --- Diagnosis ---
    @PostMapping("/diagnosis")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Add clinical diagnosis")
    public ResponseEntity<BaseResponse<ClinicalDiagnosis>> addDiagnosis(
            @RequestBody ClinicalDiagnosis diagnosis,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long visitId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Diagnosis added", null, clinicalService.addDiagnosis(diagnosis, patientId, doctorId, visitId)));
    }

    @GetMapping("/diagnosis/patient/{patientId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Page<ClinicalDiagnosis>>> getDiagnosesByPatient(
            @PathVariable Long patientId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Diagnoses found", null, clinicalService.getDiagnosesByPatient(patientId, PageRequest.of(page, size))));
    }

    // --- Doctor Notes ---
    @PostMapping("/doctor-note")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Add doctor note")
    public ResponseEntity<BaseResponse<DoctorNote>> addDoctorNote(
            @RequestBody DoctorNote note, @RequestParam Long patientId, @RequestParam Long doctorId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor note added", null, clinicalService.addDoctorNote(note, patientId, doctorId)));
    }

    @GetMapping("/doctor-note/patient/{patientId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Page<DoctorNote>>> getDoctorNotes(
            @PathVariable Long patientId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Doctor notes found", null, clinicalService.getDoctorNotesByPatient(patientId, PageRequest.of(page, size))));
    }

    // --- Prescriptions ---
    @PostMapping("/prescription")
    @PreAuthorize("hasAuthority('CLINICAL_WRITE')")
    @Operation(summary = "Add clinical prescription")
    public ResponseEntity<BaseResponse<ClinicalPrescription>> addPrescription(
            @RequestBody ClinicalPrescription prescription,
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam(required = false) Long visitId) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Prescription added", null, clinicalService.addPrescription(prescription, patientId, doctorId, visitId)));
    }

    @GetMapping("/prescription/patient/{patientId}")
    @PreAuthorize("hasAuthority('CLINICAL_READ')")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Page<ClinicalPrescription>>> getPrescriptionsByPatient(
            @PathVariable Long patientId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Prescriptions found", null, clinicalService.getPrescriptionsByPatient(patientId, PageRequest.of(page, size))));
    }
}

