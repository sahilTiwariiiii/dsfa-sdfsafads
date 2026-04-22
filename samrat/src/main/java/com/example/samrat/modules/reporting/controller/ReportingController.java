package com.example.samrat.modules.reporting.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.reporting.entity.SystemReport;
import com.example.samrat.modules.reporting.service.ReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reporting")
@RequiredArgsConstructor
@Tag(name = "Reporting Management", description = "APIs for generating and retrieving hospital performance reports")
public class ReportingController {

    private final ReportingService reportingService;

    @PostMapping("/generate")
    @PreAuthorize("hasAuthority('REPORT_WRITE')")
    @Operation(summary = "Generate system report", description = "Generates a new performance or clinical report in PDF/Excel format")
    public ResponseEntity<BaseResponse<SystemReport>> generateReport(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String format,
            @RequestParam String generatedBy) {
        SystemReport report = reportingService.generateReport(name, type, format, generatedBy);
        return ResponseEntity.ok(new BaseResponse<>(true, "Report generated successfully", null, report));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('REPORT_READ')")
    @Operation(summary = "Get all reports", description = "Retrieves a list of all reports generated for the current hospital")
    public ResponseEntity<BaseResponse<List<SystemReport>>> getReports() {
        List<SystemReport> reports = reportingService.getReports();
        return ResponseEntity.ok(new BaseResponse<>(true, "Reports found", null, reports));
    }
}
