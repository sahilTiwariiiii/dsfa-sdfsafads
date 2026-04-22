package com.example.samrat.modules.finance.accounts.repository;

import com.example.samrat.modules.finance.accounts.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {
    List<FinancialTransaction> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);
}
