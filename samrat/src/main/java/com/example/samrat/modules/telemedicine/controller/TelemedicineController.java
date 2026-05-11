package com.example.samrat.modules.telemedicine.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.telemedicine.entity.TelemedicineConsultation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telemedicine")
@RequiredArgsConstructor
@Tag(name = "Telemedicine", description = "APIs for remote video consultations")
public class TelemedicineController {

    @PostMapping("/initiate")
    @PreAuthorize("hasAuthority('TELEMEDICINE_WRITE')")
    @Operation(summary = "Initiate video call", description = "Generates a meeting link and starts the consultation")
    public ResponseEntity<BaseResponse<TelemedicineConsultation>> initiateConsultation(@RequestBody TelemedicineConsultation consultation) {
        // Mock initiation logic
        consultation.setMeetingUrl("https://meet.jit.si/samrat-hms-" + consultation.getAppointmentId());
        consultation.setStatus(TelemedicineConsultation.ConsultationStatus.IN_PROGRESS);
        return ResponseEntity.ok(new BaseResponse<>(true, "Telemedicine consultation initiated", null, consultation));
    }
}

