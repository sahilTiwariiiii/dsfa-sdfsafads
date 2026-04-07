package com.example.samrat.modules.billing.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.billing.entity.Invoice;
import com.example.samrat.modules.billing.entity.InvoiceItem;
import com.example.samrat.modules.billing.repository.InvoiceRepository;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.repository.AdmissionRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;
    private final AdmissionRepository admissionRepository;

    @Transactional
    public Invoice generateOPDInvoice(Long patientId, Double consultationFee) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Invoice invoice = new Invoice();
        invoice.setPatient(patient);
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setPaymentMethod("CASH");
        invoice.setStatus(Invoice.InvoiceStatus.GENERATED);
        invoice.setHospitalId(TenantContext.getHospitalId());
        invoice.setBranchId(TenantContext.getBranchId());

        InvoiceItem item = new InvoiceItem();
        item.setInvoice(invoice);
        item.setItemName("Consultation Fee");
        item.setUnitPrice(consultationFee);
        item.setQuantity(1.0);
        item.setTaxAmount(0.0);
        item.setTotalAmount(consultationFee);
        item.setCategory("CONSULTATION");

        invoice.setItems(new ArrayList<>());
        invoice.getItems().add(item);
        invoice.setTotalAmount(consultationFee);
        invoice.setNetAmount(consultationFee);
        invoice.setDueAmount(consultationFee);
        invoice.setPaidAmount(0.0);

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice generateIPDInvoice(Long admissionId) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new RuntimeException("Admission not found"));

        // Logic to calculate bed charges, service charges, pharmacy, lab, etc.
        // Simplified version:
        Invoice invoice = new Invoice();
        invoice.setPatient(admission.getPatient());
        invoice.setAdmission(admission);
        invoice.setInvoiceNumber("IPD-INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setHospitalId(TenantContext.getHospitalId());
        invoice.setBranchId(TenantContext.getBranchId());

        // Calculate total from all service orders (simplified)
        double total = 5000.0; // dummy total
        invoice.setTotalAmount(total);
        invoice.setNetAmount(total);
        invoice.setDueAmount(total);
        invoice.setPaidAmount(0.0);
        invoice.setStatus(Invoice.InvoiceStatus.GENERATED);

        return invoiceRepository.save(invoice);
    }
}
