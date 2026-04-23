package com.example.samrat.modules.diagnostics.lab.repository;

import com.example.samrat.modules.diagnostics.lab.entity.LabOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    List<LabOrder> findByPatientId(Long patientId);
    Page<LabOrder> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT l FROM LabOrder l WHERE l.hospitalId = :hospitalId AND l.branchId = :branchId " +
           "AND (:patientId IS NULL OR l.patient.id = :patientId) " +
           "AND (:doctorId IS NULL OR l.orderingDoctor.id = :doctorId) " +
           "AND (:status IS NULL OR l.status = :status) " +
           "AND (:startTime IS NULL OR l.orderTime >= :startTime) " +
           "AND (:endTime IS NULL OR l.orderTime <= :endTime)")
    Page<LabOrder> searchLabOrders(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("status") LabOrder.LabStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
}
