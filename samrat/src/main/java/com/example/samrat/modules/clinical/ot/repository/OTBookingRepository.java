package com.example.samrat.modules.clinical.ot.repository;

import com.example.samrat.modules.clinical.ot.entity.OTBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OTBookingRepository extends JpaRepository<OTBooking, Long> {
    Page<OTBooking> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT b FROM OTBooking b WHERE b.hospitalId = :hospitalId AND b.branchId = :branchId " +
           "AND (:patientId IS NULL OR b.patient.id = :patientId) " +
           "AND (:surgeonId IS NULL OR b.surgeon.id = :surgeonId) " +
           "AND (:departmentId IS NULL OR b.department.id = :departmentId) " +
           "AND (:status IS NULL OR b.status = :status) " +
           "AND (:startTime IS NULL OR b.scheduleDate >= :startTime) " +
           "AND (:endTime IS NULL OR b.scheduleDate <= :endTime)")
    Page<OTBooking> searchOTBookings(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("surgeonId") Long surgeonId,
            @Param("departmentId") Long departmentId,
            @Param("status") OTBooking.OTStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
}
