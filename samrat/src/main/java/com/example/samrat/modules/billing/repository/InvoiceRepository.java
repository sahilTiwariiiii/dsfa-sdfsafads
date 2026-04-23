package com.example.samrat.modules.billing.repository;

import com.example.samrat.modules.billing.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPatientId(Long patientId);
    Page<Invoice> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.hospitalId = :hospitalId AND i.branchId = :branchId " +
           "AND (:patientId IS NULL OR i.patient.id = :patientId) " +
           "AND (:invoiceNumber IS NULL OR i.invoiceNumber LIKE %:invoiceNumber%) " +
           "AND (:status IS NULL OR i.status = :status) " +
           "AND (:startTime IS NULL OR i.invoiceDate >= :startTime) " +
           "AND (:endTime IS NULL OR i.invoiceDate <= :endTime)")
    Page<Invoice> searchInvoices(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("invoiceNumber") String invoiceNumber,
            @Param("status") Invoice.InvoiceStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    java.util.Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
