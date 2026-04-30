package com.example.samrat.modules.clinical.vitals.repository;

import com.example.samrat.modules.clinical.vitals.entity.PatientVital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientVitalRepository extends JpaRepository<PatientVital, Long> {
    List<PatientVital> findByPatientIdOrderByRecordedAtDesc(Long patientId);
    
    List<PatientVital> findByHospitalIdAndBranchIdAndPatientIdOrderByRecordedAtDesc(Long hospitalId, Long branchId, Long patientId);
}
