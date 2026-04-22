package com.example.samrat.modules.finance.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.finance.accounts.entity.FinancialTransaction;
import com.example.samrat.modules.finance.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
@Tag(name = "Finance Management", description = "APIs for financial transactions and account tracking")
public class FinanceController {

    private final FinanceService financeService;

    @PostMapping("/transaction")
    @PreAuthorize("hasAuthority('FINANCE_WRITE')")
    @Operation(summary = "Record financial transaction", description = "Records a new debit or credit transaction in the system")
    public ResponseEntity<BaseResponse<FinancialTransaction>> recordTransaction(
            @RequestParam String type,
            @RequestParam Double amount,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam String paymentMethod) {
        FinancialTransaction transaction = financeService.recordTransaction(type, amount, category, description, paymentMethod);
        return ResponseEntity.ok(new BaseResponse<>(true, "Transaction recorded successfully", null, transaction));
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasAuthority('FINANCE_READ')")
    @Operation(summary = "Get all transactions", description = "Retrieves all financial records for the current hospital branch")
    public ResponseEntity<BaseResponse<List<FinancialTransaction>>> getAllTransactions() {
        List<FinancialTransaction> transactions = financeService.getAllTransactions();
        return ResponseEntity.ok(new BaseResponse<>(true, "Transactions found", null, transactions));
    }
}
