package com.example.samrat.modules.patient.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.patient.dto.PatientDTO;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PatientDTO registerPatient(PatientDTO patientDTO) {
        Patient patient = modelMapper.map(patientDTO, Patient.class);
        patient.setHospitalId(TenantContext.getHospitalId());
        patient.setBranchId(TenantContext.getBranchId());

        // Generate UHID if not provided
        if (patient.getUhid() == null) {
            patient.setUhid("UHID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Handle family mapping
        if (patientDTO.getFamilyHeadId() != null) {
            Patient familyHead = patientRepository.findById(patientDTO.getFamilyHeadId())
                    .orElseThrow(() -> new RuntimeException("Family head not found"));
            patient.setFamilyHead(familyHead);
        }

        Patient savedPatient = patientRepository.save(patient);
        return modelMapper.map(savedPatient, PatientDTO.class);
    }

    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        return patientRepository.findByHospitalIdAndBranchId(
                        TenantContext.getHospitalId(), TenantContext.getBranchId(), pageable)
                .map(patient -> modelMapper.map(patient, PatientDTO.class));
    }

    public PatientDTO getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Transactional
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        modelMapper.map(patientDTO, existingPatient);
        existingPatient.setId(id); // Ensure ID is not overwritten

        Patient updatedPatient = patientRepository.save(existingPatient);
        return modelMapper.map(updatedPatient, PatientDTO.class);
    }

    public List<PatientDTO> getPatientsByPhoneNumber(String phoneNumber) {
        return patientRepository.findByHospitalIdAndBranchIdAndPhoneNumber(
                        TenantContext.getHospitalId(), TenantContext.getBranchId(), phoneNumber)
                .stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }

    public PatientDTO getPatientByUhid(String uhid) {
        return patientRepository.findByHospitalIdAndBranchIdAndUhid(
                        TenantContext.getHospitalId(), TenantContext.getBranchId(), uhid)
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public List<PatientDTO> getFamilyMembers(Long patientId) {
        return patientRepository.findByFamilyHeadId(patientId)
                .stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }

    public Page<PatientDTO> searchPatients(String query, Pageable pageable) {
        return patientRepository.searchPatients(
                        TenantContext.getHospitalId(), TenantContext.getBranchId(), query, pageable)
                .map(patient -> modelMapper.map(patient, PatientDTO.class));
    }

    @Transactional
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
