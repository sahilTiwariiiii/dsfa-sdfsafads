package com.example.samrat.modules.diagnostics.radiology.repository;

import com.example.samrat.modules.diagnostics.radiology.entity.RadiologyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RadiologyOrderRepository extends JpaRepository<RadiologyOrder, Long> {
    Page<RadiologyOrder> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT r FROM RadiologyOrder r WHERE r.hospitalId = :hospitalId AND r.branchId = :branchId " +
           "AND (:patientId IS NULL OR r.patient.id = :patientId) " +
           "AND (:doctorId IS NULL OR r.orderingDoctor.id = :doctorId) " +
           "AND (:status IS NULL OR r.status = :status) " +
           "AND (:startTime IS NULL OR r.orderTime >= :startTime) " +
           "AND (:endTime IS NULL OR r.orderTime <= :endTime)")
    Page<RadiologyOrder> searchRadiologyOrders(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("status") RadiologyOrder.RadiologyStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
}
