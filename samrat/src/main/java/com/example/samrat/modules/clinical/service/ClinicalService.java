package com.example.samrat.modules.clinical.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.clinical.emr.entity.EMRRecord;
import com.example.samrat.modules.clinical.emr.repository.EMRRepository;
import com.example.samrat.modules.clinical.nursing.entity.NursingNote;
import com.example.samrat.modules.clinical.nursing.repository.NursingNoteRepository;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.repository.AdmissionRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicalService {

    private final EMRRepository emrRepository;
    private final NursingNoteRepository nursingNoteRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdmissionRepository admissionRepository;

    @Transactional
    public EMRRecord createEMRRecord(Long patientId, Long doctorId, String complaint, String diagnosis, String prescription) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        EMRRecord record = new EMRRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setChiefComplaint(complaint);
        record.setDiagnosis(diagnosis);
        record.setPrescription(prescription);
        record.setHospitalId(TenantContext.getHospitalId());
        record.setBranchId(TenantContext.getBranchId());

        return emrRepository.save(record);
    }

    @Transactional
    public NursingNote addNursingNote(Long admissionId, String description, Double temp, Double pulse, String status) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new RuntimeException("Admission not found"));

        NursingNote note = new NursingNote();
        note.setAdmission(admission);
        note.setNoteTime(LocalDateTime.now());
        note.setDescription(description);
        note.setTemperature(temp);
        note.setPulse(pulse);
        note.setStatus(status);
        note.setHospitalId(TenantContext.getHospitalId());
        note.setBranchId(TenantContext.getBranchId());

        return nursingNoteRepository.save(note);
    }
}
