package com.example.samrat.modules.billing.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.billing.entity.Invoice;
import com.example.samrat.modules.billing.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
@Tag(name = "Billing Management", description = "APIs for managing patient invoices and payments")
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/opd")
    @PreAuthorize("hasAuthority('BILLING_WRITE')")
    @Operation(summary = "Generate OPD invoice", description = "Creates a new invoice for an outpatient consultation")
    public ResponseEntity<BaseResponse<Invoice>> generateOPDInvoice(
            @RequestParam Long patientId,
            @RequestParam Double consultationFee) {
        Invoice invoice = billingService.generateOPDInvoice(patientId, consultationFee);
        return ResponseEntity.ok(new BaseResponse<>(true, "OPD Invoice generated successfully", null, invoice));
    }

    @PostMapping("/ipd")
    @PreAuthorize("hasAuthority('BILLING_WRITE')")
    @Operation(summary = "Generate IPD invoice", description = "Creates a new invoice for an inpatient admission")
    public ResponseEntity<BaseResponse<Invoice>> generateIPDInvoice(@RequestParam Long admissionId) {
        Invoice invoice = billingService.generateIPDInvoice(admissionId);
        return ResponseEntity.ok(new BaseResponse<>(true, "IPD Invoice generated successfully", null, invoice));
    }
}
