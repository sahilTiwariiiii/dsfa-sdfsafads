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

import com.example.samrat.modules.admin.repository.DepartmentRepository;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.opd.entity.OPDVisit;
import com.example.samrat.modules.opd.repository.OPDVisitRepository;
import com.example.samrat.modules.patient.dto.PatientRegistrationRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final OPDVisitRepository opdVisitRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PatientDTO registerPatientFromRequest(PatientRegistrationRequest request) {
        Patient patient = new Patient();
        // Map fields from request to entity
        String[] nameParts = request.getPatientName().split(" ");
        patient.setFirstName(nameParts[0]);
        patient.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        patient.setGender(request.getGender());
        patient.setPhoneNumber(request.getMobile());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setMaritalStatus(request.getMaritalStatus());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setGuardianName(request.getGuardianName());
        
        if (request.getDob() != null && !request.getDob().isEmpty()) {
            patient.setDateOfBirth(LocalDate.parse(request.getDob()));
        }

        patient.setHospitalId(TenantContext.getHospitalId());
        patient.setBranchId(TenantContext.getBranchId());

        if (patient.getUhid() == null) {
            patient.setUhid("UHID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        Patient savedPatient = patientRepository.save(patient);

        // If visit type is OPD, create an OPD visit
        if ("OPD".equalsIgnoreCase(request.getVisitType())) {
            OPDVisit opdVisit = new OPDVisit();
            opdVisit.setPatient(savedPatient);
            
            if (request.getDoctorId() != null) {
                // Assuming doctorId in request is Long, if it's String we need to parse it
                try {
                    Long docId = Long.parseLong(request.getDoctorId());
                    opdVisit.setDoctor(doctorRepository.findById(docId).orElse(null));
                } catch (NumberFormatException e) {
                    // Ignore or handle
                }
            }
            
            if (request.getDepartmentId() != null) {
                try {
                    Long deptId = Long.parseLong(request.getDepartmentId());
                    opdVisit.setDepartment(departmentRepository.findById(deptId).orElse(null));
                } catch (NumberFormatException e) {
                    // Ignore or handle
                }
            }

            opdVisit.setVisitTime(LocalDateTime.now());
            opdVisit.setVisitType(request.getVisitType());
            opdVisit.setSlot(request.getSlot());
            opdVisit.setFee(request.getFee());
            opdVisit.setTokenNumber("WALK-IN-" + System.currentTimeMillis() % 1000);
            opdVisit.setStatus(OPDVisit.VisitStatus.WAITING);
            opdVisit.setHospitalId(TenantContext.getHospitalId());
            opdVisit.setBranchId(TenantContext.getBranchId());

            opdVisitRepository.save(opdVisit);
        }

        return modelMapper.map(savedPatient, PatientDTO.class);
    }

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
