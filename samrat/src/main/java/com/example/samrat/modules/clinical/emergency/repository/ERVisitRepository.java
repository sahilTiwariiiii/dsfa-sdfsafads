package com.example.samrat.modules.clinical.emergency.repository;

import com.example.samrat.modules.clinical.emergency.entity.ERVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ERVisitRepository extends JpaRepository<ERVisit, Long> {
    Page<ERVisit> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT e FROM ERVisit e WHERE e.hospitalId = :hospitalId AND e.branchId = :branchId " +
           "AND (:patientId IS NULL OR e.patient.id = :patientId) " +
           "AND (:doctorId IS NULL OR e.assignedDoctor.id = :doctorId) " +
           "AND (:triage IS NULL OR e.triage = :triage) " +
           "AND (:status IS NULL OR e.status = :status) " +
           "AND (:startTime IS NULL OR e.arrivalTime >= :startTime) " +
           "AND (:endTime IS NULL OR e.arrivalTime <= :endTime)")
    Page<ERVisit> searchERVisits(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("triage") ERVisit.TriageStatus triage,
            @Param("status") ERVisit.ERStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
}
