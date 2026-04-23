package com.example.samrat.modules.opd.repository;

import com.example.samrat.modules.opd.entity.OPDVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OPDVisitRepository extends JpaRepository<OPDVisit, Long> {
    List<OPDVisit> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);

    Page<OPDVisit> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT v FROM OPDVisit v WHERE v.hospitalId = :hospitalId AND v.branchId = :branchId " +
           "AND (:patientId IS NULL OR v.patient.id = :patientId) " +
           "AND (:doctorId IS NULL OR v.doctor.id = :doctorId) " +
           "AND (:departmentId IS NULL OR v.department.id = :departmentId) " +
           "AND (:status IS NULL OR v.status = :status) " +
           "AND (:startTime IS NULL OR v.visitTime >= :startTime) " +
           "AND (:endTime IS NULL OR v.visitTime <= :endTime)")
    Page<OPDVisit> searchVisits(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("departmentId") Long departmentId,
            @Param("status") OPDVisit.VisitStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    List<OPDVisit> findByPatientId(Long patientId);
}
