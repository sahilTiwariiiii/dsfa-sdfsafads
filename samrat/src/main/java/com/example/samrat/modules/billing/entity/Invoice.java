package com.example.samrat.modules.billing.entity;

import com.example.samrat.core.entity.BaseEntity;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class Invoice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id")
    private Admission admission; // Null for OPD

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private LocalDateTime invoiceDate;

    @Column(nullable = false)
    private Double totalAmount;

    private Double taxAmount;
    private Double discountAmount;
    private Double netAmount;
    private Double paidAmount;
    private Double dueAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Column(nullable = false)
    private String paymentMethod; // CASH, CARD, ONLINE, INSURANCE

    // Insurance/TPA details
    private String tpaName;
    private String insurancePolicyNumber;
    private Double insuranceApprovedAmount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();

    public enum InvoiceStatus {
        DRAFT, GENERATED, PAID, PARTIALLY_PAID, CANCELLED, REFUNDED
    }
}
