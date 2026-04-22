package com.example.samrat.modules.finance.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.finance.accounts.entity.FinancialTransaction;
import com.example.samrat.modules.finance.accounts.repository.FinancialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final FinancialTransactionRepository transactionRepository;

    @Transactional
    public FinancialTransaction recordTransaction(String type, Double amount, String category, String description, String paymentMethod) {
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setDescription(description);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setHospitalId(TenantContext.getHospitalId());
        transaction.setBranchId(TenantContext.getBranchId());

        return transactionRepository.save(transaction);
    }

    public List<FinancialTransaction> getAllTransactions() {
        return transactionRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId());
    }
}
