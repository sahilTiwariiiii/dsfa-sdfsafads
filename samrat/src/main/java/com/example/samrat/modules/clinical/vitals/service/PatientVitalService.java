package com.example.samrat.modules.clinical.vitals.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.clinical.vitals.dto.PatientVitalDTO;
import com.example.samrat.modules.clinical.vitals.entity.PatientVital;
import com.example.samrat.modules.clinical.vitals.repository.PatientVitalRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientVitalService {

    private final PatientVitalRepository patientVitalRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public PatientVitalDTO recordVitals(PatientVitalDTO dto) {
        Long hospitalId = TenantContext.getHospitalId();
        Long branchId = TenantContext.getBranchId();

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        PatientVital vital = new PatientVital();
        vital.setPatient(patient);
        vital.setHospitalId(hospitalId);
        vital.setBranchId(branchId);
        vital.setRecordedAt(dto.getRecordedAt() != null ? dto.getRecordedAt() : LocalDateTime.now());
        
        vital.setWeight(dto.getWeight());
        vital.setHeight(dto.getHeight());
        vital.setBloodPressure(dto.getBloodPressure());
        vital.setTemperature(dto.getTemperature());
        vital.setPulseRate(dto.getPulseRate());
        vital.setRespiratoryRate(dto.getRespiratoryRate());
        vital.setSpo2(dto.getSpo2());
        
        vital.setRecordedBy(dto.getRecordedBy());
        vital.setRemark(dto.getRemark());
        vital.setOpdVisitId(dto.getOpdVisitId());
        vital.setAdmissionId(dto.getAdmissionId());

        return toDTO(patientVitalRepository.save(vital));
    }

    @Transactional(readOnly = true)
    public List<PatientVitalDTO> getPatientVitalsHistory(Long patientId) {
        Long hospitalId = TenantContext.getHospitalId();
        Long branchId = TenantContext.getBranchId();
        
        return patientVitalRepository.findByHospitalIdAndBranchIdAndPatientIdOrderByRecordedAtDesc(hospitalId, branchId, patientId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PatientVitalDTO toDTO(PatientVital vital) {
        PatientVitalDTO dto = new PatientVitalDTO();
        dto.setId(vital.getId());
        dto.setPatientId(vital.getPatient().getId());
        dto.setRecordedAt(vital.getRecordedAt());
        dto.setWeight(vital.getWeight());
        dto.setHeight(vital.getHeight());
        dto.setBloodPressure(vital.getBloodPressure());
        dto.setTemperature(vital.getTemperature());
        dto.setPulseRate(vital.getPulseRate());
        dto.setRespiratoryRate(vital.getRespiratoryRate());
        dto.setSpo2(vital.getSpo2());
        dto.setRecordedBy(vital.getRecordedBy());
        dto.setRemark(vital.getRemark());
        dto.setOpdVisitId(vital.getOpdVisitId());
        dto.setAdmissionId(vital.getAdmissionId());
        return dto;
    }
}
