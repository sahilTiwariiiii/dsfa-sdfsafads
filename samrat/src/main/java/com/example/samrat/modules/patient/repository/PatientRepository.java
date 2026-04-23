package com.example.samrat.modules.patient.repository;

import com.example.samrat.modules.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByHospitalIdAndBranchIdAndPhoneNumber(Long hospitalId, Long branchId, String phoneNumber);
    Optional<Patient> findByHospitalIdAndBranchIdAndUhid(Long hospitalId, Long branchId, String uhid);
    List<Patient> findByFamilyHeadId(Long familyHeadId);

    Page<Patient> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.hospitalId = :hospitalId AND p.branchId = :branchId " +
           "AND (:query IS NULL OR " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "p.phoneNumber LIKE CONCAT('%', :query, '%') OR " +
           "p.uhid LIKE CONCAT('%', :query, '%'))")
    Page<Patient> searchPatients(@Param("hospitalId") Long hospitalId, @Param("branchId") Long branchId, @Param("query") String query, Pageable pageable);
}
