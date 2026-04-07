package com.example.samrat.modules.patient.repository;

import com.example.samrat.modules.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByHospitalIdAndBranchIdAndPhoneNumber(Long hospitalId, Long branchId, String phoneNumber);
    Optional<Patient> findByHospitalIdAndBranchIdAndUhid(Long hospitalId, Long branchId, String uhid);
    List<Patient> findByFamilyHeadId(Long familyHeadId);
}
