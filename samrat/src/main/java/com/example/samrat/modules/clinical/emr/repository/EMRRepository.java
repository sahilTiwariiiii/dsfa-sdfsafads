package com.example.samrat.modules.clinical.emr.repository;

import com.example.samrat.modules.clinical.emr.entity.EMRRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EMRRepository extends JpaRepository<EMRRecord, Long> {
    List<EMRRecord> findByPatientId(Long patientId);

    Page<EMRRecord> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT r FROM EMRRecord r WHERE r.hospitalId = :hospitalId AND r.branchId = :branchId " +
           "AND (:patientId IS NULL OR r.patient.id = :patientId) " +
           "AND (:doctorId IS NULL OR r.doctor.id = :doctorId) " +
           "AND (:departmentId IS NULL OR r.department.id = :departmentId) " +
           "AND (:status IS NULL OR r.status = :status) " +
           "AND (:keyword IS NULL OR LOWER(r.chiefComplaint) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(r.diagnosis) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<EMRRecord> searchEMRRecords(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("departmentId") Long departmentId,
            @Param("status") String status,
            @Param("keyword") String keyword,
            Pageable pageable);
}
