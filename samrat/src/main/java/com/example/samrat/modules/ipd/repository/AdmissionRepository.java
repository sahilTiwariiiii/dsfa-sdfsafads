package com.example.samrat.modules.ipd.repository;

import com.example.samrat.modules.ipd.entity.Admission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    Page<Admission> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT a FROM Admission a WHERE a.hospitalId = :hospitalId AND a.branchId = :branchId " +
           "AND (:patientId IS NULL OR a.patient.id = :patientId) " +
           "AND (:doctorId IS NULL OR a.primaryDoctor.id = :doctorId) " +
           "AND (:departmentId IS NULL OR a.department.id = :departmentId) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:startTime IS NULL OR a.admissionDate >= :startTime) " +
           "AND (:endTime IS NULL OR a.admissionDate <= :endTime)")
    Page<Admission> searchAdmissions(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("departmentId") Long departmentId,
            @Param("status") Admission.AdmissionStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    @Query("SELECT a FROM Admission a WHERE a.hospitalId = :hospitalId AND a.branchId = :branchId " +
           "AND a.patient.id = :patientId AND a.status = 'ADMITTED'")
    java.util.Optional<Admission> findActiveByPatientId(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId);
}
