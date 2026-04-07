package com.example.samrat.modules.billing.repository;

import com.example.samrat.modules.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPatientId(Long patientId);
    List<Invoice> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);
}
