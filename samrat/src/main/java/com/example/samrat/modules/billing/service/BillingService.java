package com.example.samrat.modules.billing.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.billing.entity.Invoice;
import com.example.samrat.modules.billing.entity.InvoiceItem;
import com.example.samrat.modules.billing.entity.ServiceCharge;
import com.example.samrat.modules.billing.repository.InvoiceRepository;
import com.example.samrat.modules.billing.repository.ServiceChargeRepository;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.repository.AdmissionRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final ServiceChargeRepository serviceChargeRepository;
    private final PatientRepository patientRepository;
    private final AdmissionRepository admissionRepository;

    // --- Service Charge Management ---

    @Transactional
    public ServiceCharge createServiceCharge(ServiceCharge serviceCharge) {
        serviceCharge.setHospitalId(TenantContext.getHospitalId());
        serviceCharge.setBranchId(TenantContext.getBranchId());
        return serviceChargeRepository.save(serviceCharge);
    }

    public Page<ServiceCharge> getAllServiceCharges(Pageable pageable) {
        return serviceChargeRepository.findByHospitalIdAndBranchId(TenantContext.getHospitalId(), TenantContext.getBranchId(), pageable);
    }

    // --- Invoice Management ---

    @Transactional
    public Invoice createInvoice(Invoice invoice, Long patientId, Long admissionId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        invoice.setPatient(patient);
        if (admissionId != null) {
            Admission admission = admissionRepository.findById(admissionId)
                    .orElseThrow(() -> new RuntimeException("Admission not found"));
            invoice.setAdmission(admission);
        }
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis() % 10000000);
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setHospitalId(TenantContext.getHospitalId());
        invoice.setBranchId(TenantContext.getBranchId());

        // Calculate totals from items
        double total = 0;
        double tax = 0;
        if (invoice.getItems() != null) {
            for (InvoiceItem item : invoice.getItems()) {
                item.setInvoice(invoice);
                total += item.getTotalAmount();
                tax += (item.getTaxAmount() != null ? item.getTaxAmount() : 0);
            }
        }
        invoice.setTotalAmount(total);
        invoice.setTaxAmount(tax);
        invoice.setNetAmount(total + tax - (invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : 0));
        invoice.setDueAmount(invoice.getNetAmount() - (invoice.getPaidAmount() != null ? invoice.getPaidAmount() : 0));

        return invoiceRepository.save(invoice);
    }

    public Page<Invoice> searchInvoices(Long patientId, String invoiceNumber, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        Invoice.InvoiceStatus invoiceStatus = status != null ? Invoice.InvoiceStatus.valueOf(status.toUpperCase()) : null;
        return invoiceRepository.searchInvoices(TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, invoiceNumber, invoiceStatus, start, end, pageable);
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    @Transactional
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    @Transactional
    public Invoice updatePayment(Long invoiceId, Double amount, String method) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        double currentPaid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : 0;
        invoice.setPaidAmount(currentPaid + amount);
        invoice.setDueAmount(invoice.getNetAmount() - invoice.getPaidAmount());
        invoice.setPaymentMethod(method);

        if (invoice.getDueAmount() <= 0) {
            invoice.setStatus(Invoice.InvoiceStatus.PAID);
        } else {
            invoice.setStatus(Invoice.InvoiceStatus.PARTIALLY_PAID);
        }

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice cancelInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus(Invoice.InvoiceStatus.CANCELLED);
        return invoiceRepository.save(invoice);
    }
}
