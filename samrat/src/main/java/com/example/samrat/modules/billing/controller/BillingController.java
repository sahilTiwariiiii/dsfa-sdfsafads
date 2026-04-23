package com.example.samrat.modules.billing.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.billing.entity.Invoice;
import com.example.samrat.modules.billing.entity.ServiceCharge;
import com.example.samrat.modules.billing.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
@Tag(name = "V1 - billingRoute", description = "Enterprise APIs for service charges, packages, billing groups, and patient invoicing")
public class BillingController {

    private final BillingService billingService;

    @GetMapping
    @Operation(summary = "List V1 - billingRoute")
    public ResponseEntity<BaseResponse<Page<Invoice>>> listBillingV1(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ResponseEntity.ok(new BaseResponse<>(true, "Billing list", null, billingService.searchInvoices(null, null, null, null, null, pageable)));
    }

    @PostMapping
    @Operation(summary = "Create V1 - billingRoute")
    public ResponseEntity<BaseResponse<Invoice>> createBillingV1(@RequestBody Invoice invoice) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, invoice));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get V1 - billingRoute by ID")
    public ResponseEntity<BaseResponse<Invoice>> getBillingByIdV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Detail", null, null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update V1 - billingRoute")
    public ResponseEntity<BaseResponse<Invoice>> updateBillingV1(@PathVariable Long id, @RequestBody Invoice invoice) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, invoice));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete V1 - billingRoute")
    public ResponseEntity<BaseResponse<Void>> deleteBillingV1(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Deleted", null, null));
    }

    // --- Service Charge Management ---

    @PostMapping("/service-charges")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    @Operation(summary = "Create service charge")
    public ResponseEntity<BaseResponse<ServiceCharge>> createServiceCharge(@RequestBody ServiceCharge charge) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Service charge created", null, billingService.createServiceCharge(charge)));
    }

    @GetMapping("/service-charges")
    @PreAuthorize("hasAuthority('BILLING_READ')")
    @Operation(summary = "Get all service charges")
    public ResponseEntity<BaseResponse<Page<ServiceCharge>>> getAllServiceCharges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Service charges found", null, billingService.getAllServiceCharges(pageable)));
    }

    // --- Invoice Management ---

    @PostMapping("/invoices")
    @PreAuthorize("hasAuthority('BILLING_WRITE')")
    @Operation(summary = "Create invoice", description = "Creates a new invoice for a patient (OPD or IPD)")
    public ResponseEntity<BaseResponse<Invoice>> createInvoice(
            @RequestBody Invoice invoice,
            @RequestParam Long patientId,
            @RequestParam(required = false) Long admissionId) {
        Invoice created = billingService.createInvoice(invoice, patientId, admissionId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Invoice created successfully", null, created));
    }

    @GetMapping("/invoices/search")
    @PreAuthorize("hasAuthority('BILLING_READ')")
    @Operation(summary = "Search invoices")
    public ResponseEntity<BaseResponse<Page<Invoice>>> searchInvoices(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String invoiceNumber,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Invoices found", null, billingService.searchInvoices(patientId, invoiceNumber, status, start, end, pageable)));
    }

    @PatchMapping("/invoices/{id}/payment")
    @PreAuthorize("hasAuthority('BILLING_WRITE')")
    @Operation(summary = "Update invoice payment")
    public ResponseEntity<BaseResponse<Invoice>> updatePayment(
            @PathVariable Long id,
            @RequestParam Double amount,
            @RequestParam String method) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Payment updated successfully", null, billingService.updatePayment(id, amount, method)));
    }

    @DeleteMapping("/invoices/{id}")
    @PreAuthorize("hasAuthority('BILLING_WRITE')")
    @Operation(summary = "Cancel invoice")
    public ResponseEntity<BaseResponse<Invoice>> cancelInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Invoice cancelled successfully", null, billingService.cancelInvoice(id)));
    }
}
