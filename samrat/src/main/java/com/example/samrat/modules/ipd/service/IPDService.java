package com.example.samrat.modules.ipd.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.entity.Bed;
import com.example.samrat.modules.ipd.entity.Ward;
import com.example.samrat.modules.ipd.entity.Admission.AdmissionStatus;
import com.example.samrat.modules.ipd.repository.AdmissionRepository;
import com.example.samrat.modules.ipd.repository.BedRepository;
import com.example.samrat.modules.ipd.repository.WardRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IPDService {

    private final AdmissionRepository admissionRepository;
    private final BedRepository bedRepository;
    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    // --- Ward Management ---

    @Transactional
    public Ward createWard(Ward ward, Long departmentId) {
        if (departmentId != null) {
            ward.setDepartment(departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found")));
        }
        ward.setHospitalId(TenantContext.getHospitalId());
        ward.setBranchId(TenantContext.getBranchId());
        return wardRepository.save(ward);
    }

    public Page<Ward> getAllWards(Pageable pageable) {
        return wardRepository.findByHospitalIdAndBranchId(TenantContext.getHospitalId(), TenantContext.getBranchId(), pageable);
    }

    // --- Bed Management ---

    @Transactional
    public Bed createBed(Bed bed, Long wardId) {
        Ward ward = wardRepository.findById(wardId).orElseThrow(() -> new RuntimeException("Ward not found"));
        bed.setWard(ward);
        bed.setHospitalId(TenantContext.getHospitalId());
        bed.setBranchId(TenantContext.getBranchId());
        return bedRepository.save(bed);
    }

    public Page<Bed> getAllBeds(Pageable pageable) {
        return bedRepository.findByHospitalIdAndBranchId(TenantContext.getHospitalId(), TenantContext.getBranchId(), pageable);
    }

    public List<Bed> getAvailableBedsByWard(Long wardId) {
        return bedRepository.findByWardIdAndStatus(wardId, Bed.BedStatus.AVAILABLE);
    }

    // --- Admission Management ---

    @Transactional
    public Admission admitPatient(Admission admission, Long patientId, Long doctorId, Long bedId, Long departmentId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new RuntimeException("Bed not found"));

        if (bed.getStatus() != Bed.BedStatus.AVAILABLE) {
            throw new RuntimeException("Bed is not available");
        }

        admission.setPatient(patient);
        admission.setAdmittingDoctor(doctor);
        admission.setPrimaryDoctor(doctor);
        admission.setBed(bed);
        if (departmentId != null) {
            admission.setDepartment(departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found")));
        }
        admission.setAdmissionDate(LocalDateTime.now());
        admission.setIpdNumber("IPD-" + System.currentTimeMillis() % 1000000);
        admission.setStatus(AdmissionStatus.ADMITTED);
        admission.setHospitalId(TenantContext.getHospitalId());
        admission.setBranchId(TenantContext.getBranchId());

        bed.setStatus(Bed.BedStatus.OCCUPIED);
        bedRepository.save(bed);

        return admissionRepository.save(admission);
    }

    public Page<Admission> searchAdmissions(Long patientId, Long doctorId, Long departmentId, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        AdmissionStatus admissionStatus = status != null ? AdmissionStatus.valueOf(status.toUpperCase()) : null;
        return admissionRepository.searchAdmissions(TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, doctorId, departmentId, admissionStatus, start, end, pageable);
    }

    @Transactional
    public Admission dischargePatient(Long admissionId) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new RuntimeException("Admission not found"));

        admission.setDischargeDate(LocalDateTime.now());
        admission.setStatus(AdmissionStatus.DISCHARGED);

        Bed bed = admission.getBed();
        bed.setStatus(Bed.BedStatus.CLEANING);
        bedRepository.save(bed);

        return admissionRepository.save(admission);
    }

    @Transactional
    public Admission transferBed(Long admissionId, Long newBedId) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new RuntimeException("Admission not found"));
        Bed newBed = bedRepository.findById(newBedId)
                .orElseThrow(() -> new RuntimeException("New Bed not found"));

        if (newBed.getStatus() != Bed.BedStatus.AVAILABLE) {
            throw new RuntimeException("New Bed is not available");
        }

        Bed oldBed = admission.getBed();
        oldBed.setStatus(Bed.BedStatus.CLEANING);
        bedRepository.save(oldBed);

        newBed.setStatus(Bed.BedStatus.OCCUPIED);
        bedRepository.save(newBed);

        admission.setBed(newBed);
        return admissionRepository.save(admission);
    }
}
