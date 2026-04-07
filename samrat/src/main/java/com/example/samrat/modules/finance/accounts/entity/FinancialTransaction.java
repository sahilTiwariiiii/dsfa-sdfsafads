package com.example.samrat.modules.finance.accounts.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transactions")
@Getter
@Setter
public class FinancialTransaction extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @Column(nullable = false)
    private String transactionType; // DEBIT, CREDIT

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String category; // OPD_BILLING, IPD_BILLING, PHARMACY, SALARY, PURCHASE

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String paymentMethod; // CASH, CARD, ONLINE, CHEQUE

    private String referenceNumber; // Bank reference or internal invoice ID

    @Column(nullable = false)
    private String status = "SUCCESS"; // SUCCESS, PENDING, FAILED, VOID
}
