package com.example.samrat.modules.ipd.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.entity.Bed;
import com.example.samrat.modules.ipd.entity.Admission.AdmissionStatus;
import com.example.samrat.modules.ipd.repository.AdmissionRepository;
import com.example.samrat.modules.ipd.repository.BedRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IPDService {

    private final AdmissionRepository admissionRepository;
    private final BedRepository bedRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public Admission admitPatient(Long patientId, Long doctorId, Long bedId, String reason) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new RuntimeException("Bed not found"));

        if (bed.getStatus() != Bed.BedStatus.AVAILABLE) {
            throw new RuntimeException("Bed is not available");
        }

        Admission admission = new Admission();
        admission.setPatient(patient);
        admission.setAdmittingDoctor(doctor);
        admission.setPrimaryDoctor(doctor);
        admission.setBed(bed);
        admission.setAdmissionDate(LocalDateTime.now());
        admission.setAdmissionReason(reason);
        admission.setStatus(AdmissionStatus.ADMITTED);
        admission.setHospitalId(TenantContext.getHospitalId());
        admission.setBranchId(TenantContext.getBranchId());

        bed.setStatus(Bed.BedStatus.OCCUPIED);
        bedRepository.save(bed);

        return admissionRepository.save(admission);
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
}
