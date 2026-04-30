package com.example.samrat.modules.patient.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.repository.UserRepository;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final OPDVisitRepository opdVisitRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PatientDTO registerPatientFromRequest(PatientRegistrationRequest request) {
        Tenant tenant = resolveTenantContext();

        // De-duplicate by mobile number within the same hospital + branch.
        // If a patient already exists for this mobile, we re-use it and only create a visit.
        Patient existing = findExistingByMobile(tenant, request.getMobile());

        Patient patient = new Patient();
        if (existing != null) {
            patient = existing;
        }

        // Map fields from request to entity
        if (existing == null) {
            String fullName = request.getPatientName().trim();
            int firstSpace = fullName.indexOf(" ");
            if (firstSpace != -1) {
                patient.setFirstName(fullName.substring(0, firstSpace));
                patient.setLastName(fullName.substring(firstSpace + 1).trim());
            } else {
                patient.setFirstName(fullName);
                patient.setLastName("");
            }
            patient.setGender(request.getGender());
            patient.setPhoneNumber(request.getMobile());
            patient.setEmail(request.getEmail());
            patient.setAddress(request.getAddress());
            patient.setMaritalStatus(request.getMaritalStatus());
            patient.setBloodGroup(request.getBloodGroup());
            patient.setGuardianName(request.getGuardianName());
        
            if (request.getDob() != null && !request.getDob().isEmpty()) {
                try {
                    patient.setDateOfBirth(LocalDate.parse(request.getDob()));
                } catch (Exception e) {
                    // Fallback or ignore invalid date
                }
            }

            patient.setHospitalId(tenant.hospitalId());
            patient.setBranchId(tenant.branchId());

            if (patient.getUhid() == null) {
                patient.setUhid("UHID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            }

            patient = patientRepository.save(patient);
        }

        // If visit type is OPD, create an OPD visit
        if ("OPD".equalsIgnoreCase(request.getVisitType()) || "Emergency".equalsIgnoreCase(request.getVisitType())) {
            OPDVisit opdVisit = new OPDVisit();
            opdVisit.setPatient(patient);
            
            if (request.getDoctorId() != null && !request.getDoctorId().isBlank()) {
                try {
                    Long docId = Long.parseLong(request.getDoctorId());
                    opdVisit.setDoctor(doctorRepository.findById(docId).orElse(null));
                } catch (NumberFormatException e) {
                    // Handle non-numeric IDs if necessary
                }
            }
            
            if (request.getDepartmentId() != null && !request.getDepartmentId().isBlank()) {
                try {
                    Long deptId = Long.parseLong(request.getDepartmentId());
                    opdVisit.setDepartment(departmentRepository.findById(deptId).orElse(null));
                } catch (NumberFormatException e) {
                    // Handle non-numeric IDs
                }
            }

            // Use visitDate from request if available, otherwise fallback to now
            LocalDateTime visitDateTime = request.getVisitDate() != null ? request.getVisitDate() : LocalDateTime.now();
            opdVisit.setVisitTime(visitDateTime);
            
            opdVisit.setVisitType(request.getVisitType());
            opdVisit.setSlot(request.getSlot());
            opdVisit.setFee(request.getFee());
            opdVisit.setTokenNumber((request.getVisitType().startsWith("E") ? "EMR-" : "WALK-IN-") + System.currentTimeMillis() % 1000);
            opdVisit.setStatus(OPDVisit.VisitStatus.WAITING);
            opdVisit.setHospitalId(tenant.hospitalId());
            opdVisit.setBranchId(tenant.branchId());
            opdVisit.setSource(request.getSource() != null ? request.getSource() : "Walk-in");

            opdVisitRepository.save(opdVisit);
        }

        return modelMapper.map(patient, PatientDTO.class);
    }

    @Transactional
    public PatientDTO registerPatient(PatientDTO patientDTO) {
        Tenant tenant = resolveTenantContext();

        // Reject duplicate registration for standalone patients so API response matches DB behavior.
        List<Patient> samePhone = patientRepository.findByHospitalIdAndBranchIdAndPhoneNumber(
                tenant.hospitalId(), tenant.branchId(), patientDTO.getPhoneNumber());
        if (!samePhone.isEmpty() && patientDTO.getFamilyHeadId() == null) {
            throw new IllegalStateException("Patient already exists with this phone number.");
        }

        Patient patient = modelMapper.map(patientDTO, Patient.class);
        patient.setHospitalId(tenant.hospitalId());
        patient.setBranchId(tenant.branchId());

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

    private Tenant resolveTenantContext() {
        Long hospitalId = TenantContext.getHospitalId();
        Long branchId = TenantContext.getBranchId();

        if (hospitalId != null && branchId != null) {
            return new Tenant(hospitalId, branchId);
        }

        Optional.ofNullable(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication())
                .map(org.springframework.security.core.Authentication::getName)
                .flatMap(userRepository::findByUsernameAndActiveTrue)
                .ifPresent(user -> {
                    TenantContext.setHospitalId(user.getHospitalId());
                    TenantContext.setBranchId(user.getBranchId());
                });

        hospitalId = TenantContext.getHospitalId();
        branchId = TenantContext.getBranchId();
        if (hospitalId == null || branchId == null) {
            throw new RuntimeException("Hospital/Branch context is missing. Please login again.");
        }
        return new Tenant(hospitalId, branchId);
    }

    private record Tenant(Long hospitalId, Long branchId) {
    }

    private Patient findExistingByMobile(Tenant tenant, String mobile) {
        if (mobile == null || mobile.isBlank()) {
            return null;
        }
        List<Patient> existing = patientRepository.findByHospitalIdAndBranchIdAndPhoneNumber(
                tenant.hospitalId(), tenant.branchId(), mobile);
        return existing.isEmpty() ? null : existing.get(0);
    }
}
