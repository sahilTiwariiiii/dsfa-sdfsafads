package com.example.samrat.modules.clinical.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import com.example.samrat.modules.clinical.discharge.entity.DischargeSummary;
import com.example.samrat.modules.clinical.discharge.repository.DischargeSummaryRepository;
import com.example.samrat.modules.clinical.emergency.entity.ERVisit;
import com.example.samrat.modules.clinical.emergency.repository.ERVisitRepository;
import com.example.samrat.modules.clinical.emr.entity.*;
import com.example.samrat.modules.clinical.emr.repository.*;
import com.example.samrat.modules.clinical.nursing.entity.NursingNote;
import com.example.samrat.modules.clinical.nursing.repository.NursingNoteRepository;
import com.example.samrat.modules.clinical.ot.entity.OTBooking;
import com.example.samrat.modules.clinical.ot.repository.OTBookingRepository;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.repository.AdmissionRepository;
import com.example.samrat.modules.opd.repository.OPDVisitRepository;
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
public class ClinicalService {

    private final EMRRepository emrRepository;
    private final NursingNoteRepository nursingNoteRepository;
    private final DischargeSummaryRepository dischargeSummaryRepository;
    private final ERVisitRepository erVisitRepository;
    private final OTBookingRepository otBookingRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdmissionRepository admissionRepository;
    private final DepartmentRepository departmentRepository;
    private final SurgicalHistoryRepository surgicalHistoryRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PersonalHistoryRepository personalHistoryRepository;
    private final ClinicalDiagnosisRepository clinicalDiagnosisRepository;
    private final DoctorNoteRepository doctorNoteRepository;
    private final ClinicalPrescriptionRepository clinicalPrescriptionRepository;
    private final OPDVisitRepository opdVisitRepository;

    // --- EMR Records ---

    @Transactional
    public EMRRecord createEMRRecord(EMRRecord record, Long patientId, Long doctorId, Long departmentId) {
        record.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        record.setDoctor(doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found")));
        if (departmentId != null) {
            record.setDepartment(departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found")));
        }
        record.setHospitalId(TenantContext.getHospitalId());
        record.setBranchId(TenantContext.getBranchId());
        return emrRepository.save(record);
    }

    public Page<EMRRecord> searchEMRRecords(Long patientId, Long doctorId, Long departmentId, String status, String keyword, Pageable pageable) {
        return emrRepository.searchEMRRecords(TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, doctorId, departmentId, status, keyword, pageable);
    }

    public EMRRecord getEMRRecordById(Long id) {
        return emrRepository.findById(id).orElseThrow(() -> new RuntimeException("EMR Record not found"));
    }

    @Transactional
    public EMRRecord updateEMRRecord(Long id, EMRRecord details) {
        EMRRecord record = getEMRRecordById(id);
        record.setChiefComplaint(details.getChiefComplaint());
        record.setDiagnosis(details.getDiagnosis());
        record.setPrescription(details.getPrescription());
        record.setLabOrders(details.getLabOrders());
        record.setRadiologyOrders(details.getRadiologyOrders());
        record.setStatus(details.getStatus());
        // Update vitals if needed
        record.setBloodPressure(details.getBloodPressure());
        record.setBodyTemperature(details.getBodyTemperature());
        return emrRepository.save(record);
    }

    // --- Nursing Notes ---

    @Transactional
    public NursingNote addNursingNote(NursingNote note, Long admissionId) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new RuntimeException("Admission not found"));
        note.setAdmission(admission);
        note.setNoteTime(LocalDateTime.now());
        note.setHospitalId(TenantContext.getHospitalId());
        note.setBranchId(TenantContext.getBranchId());
        return nursingNoteRepository.save(note);
    }

    public Page<NursingNote> getNursingNotesByAdmission(Long admissionId, Pageable pageable) {
        return nursingNoteRepository.findByAdmissionId(admissionId, pageable);
    }

    // --- Discharge Summaries ---

    @Transactional
    public DischargeSummary createDischargeSummary(DischargeSummary summary, Long admissionId) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new RuntimeException("Admission not found"));
        summary.setAdmission(admission);
        summary.setDischargeTime(LocalDateTime.now());
        summary.setHospitalId(TenantContext.getHospitalId());
        summary.setBranchId(TenantContext.getBranchId());
        
        // Update admission status to DISCHARGED
        admission.setStatus(Admission.AdmissionStatus.DISCHARGED);
        admissionRepository.save(admission);
        
        return dischargeSummaryRepository.save(summary);
    }

    public DischargeSummary getDischargeSummaryByAdmission(Long admissionId) {
        return dischargeSummaryRepository.findByAdmissionId(admissionId)
                .orElseThrow(() -> new RuntimeException("Discharge Summary not found"));
    }

    // --- ER Visits ---

    @Transactional
    public ERVisit registerERVisit(ERVisit visit, Long patientId, Long doctorId, Long departmentId) {
        visit.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        if (doctorId != null) {
            visit.setAssignedDoctor(doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found")));
        }
        if (departmentId != null) {
            visit.setDepartment(departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found")));
        }
        visit.setArrivalTime(LocalDateTime.now());
        visit.setHospitalId(TenantContext.getHospitalId());
        visit.setBranchId(TenantContext.getBranchId());
        return erVisitRepository.save(visit);
    }

    public Page<ERVisit> searchERVisits(Long patientId, Long doctorId, String triage, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        ERVisit.TriageStatus triageStatus = triage != null ? ERVisit.TriageStatus.valueOf(triage.toUpperCase()) : null;
        ERVisit.ERStatus erStatus = status != null ? ERVisit.ERStatus.valueOf(status.toUpperCase()) : null;
        return erVisitRepository.searchERVisits(TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, doctorId, triageStatus, erStatus, start, end, pageable);
    }

    // --- OT Bookings ---

    @Transactional
    public OTBooking scheduleOT(OTBooking booking, Long patientId, Long surgeonId, Long anesthetistId, Long departmentId) {
        booking.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        booking.setSurgeon(doctorRepository.findById(surgeonId).orElseThrow(() -> new RuntimeException("Surgeon not found")));
        if (anesthetistId != null) {
            booking.setAnesthetist(doctorRepository.findById(anesthetistId).orElseThrow(() -> new RuntimeException("Anesthetist not found")));
        }
        if (departmentId != null) {
            booking.setDepartment(departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found")));
        }
        booking.setHospitalId(TenantContext.getHospitalId());
        booking.setBranchId(TenantContext.getBranchId());
        return otBookingRepository.save(booking);
    }

    public Page<OTBooking> searchOTBookings(Long patientId, Long surgeonId, Long departmentId, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        OTBooking.OTStatus otStatus = status != null ? OTBooking.OTStatus.valueOf(status.toUpperCase()) : null;
        return otBookingRepository.searchOTBookings(TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, surgeonId, departmentId, otStatus, start, end, pageable);
    }

    // --- Surgical History ---
    @Transactional
    public SurgicalHistory addSurgicalHistory(SurgicalHistory history, Long patientId) {
        history.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        history.setHospitalId(TenantContext.getHospitalId());
        history.setBranchId(TenantContext.getBranchId());
        return surgicalHistoryRepository.save(history);
    }

    public Page<SurgicalHistory> getSurgicalHistoryByPatient(Long patientId, Pageable pageable) {
        return surgicalHistoryRepository.findByPatientId(patientId, pageable);
    }

    // --- Medical History ---
    @Transactional
    public MedicalHistory addMedicalHistory(MedicalHistory history, Long patientId) {
        history.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        history.setHospitalId(TenantContext.getHospitalId());
        history.setBranchId(TenantContext.getBranchId());
        return medicalHistoryRepository.save(history);
    }

    public Page<MedicalHistory> getMedicalHistoryByPatient(Long patientId, Pageable pageable) {
        return medicalHistoryRepository.findByPatientId(patientId, pageable);
    }

    // --- Personal History ---
    @Transactional
    public PersonalHistory addPersonalHistory(PersonalHistory history, Long patientId) {
        history.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        history.setHospitalId(TenantContext.getHospitalId());
        history.setBranchId(TenantContext.getBranchId());
        return personalHistoryRepository.save(history);
    }

    public Page<PersonalHistory> getPersonalHistoryByPatient(Long patientId, Pageable pageable) {
        return personalHistoryRepository.findByPatientId(patientId, pageable);
    }

    // --- Clinical Diagnosis ---
    @Transactional
    public ClinicalDiagnosis addDiagnosis(ClinicalDiagnosis diagnosis, Long patientId, Long doctorId, Long visitId) {
        diagnosis.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        diagnosis.setDoctor(doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found")));
        if (visitId != null) {
            diagnosis.setVisit(opdVisitRepository.findById(visitId).orElseThrow(() -> new RuntimeException("Visit not found")));
        }
        diagnosis.setDiagnosisTime(LocalDateTime.now());
        diagnosis.setHospitalId(TenantContext.getHospitalId());
        diagnosis.setBranchId(TenantContext.getBranchId());
        return clinicalDiagnosisRepository.save(diagnosis);
    }

    public Page<ClinicalDiagnosis> getDiagnosesByPatient(Long patientId, Pageable pageable) {
        return clinicalDiagnosisRepository.findByPatientId(patientId, pageable);
    }

    public Page<ClinicalDiagnosis> getDiagnosesByVisit(Long visitId, Pageable pageable) {
        return clinicalDiagnosisRepository.findByVisitId(visitId, pageable);
    }

    // --- Doctor Notes ---
    @Transactional
    public DoctorNote addDoctorNote(DoctorNote note, Long patientId, Long doctorId) {
        note.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        note.setDoctor(doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found")));
        note.setNoteTime(LocalDateTime.now());
        note.setHospitalId(TenantContext.getHospitalId());
        note.setBranchId(TenantContext.getBranchId());
        return doctorNoteRepository.save(note);
    }

    public Page<DoctorNote> getDoctorNotesByPatient(Long patientId, Pageable pageable) {
        return doctorNoteRepository.findByPatientId(patientId, pageable);
    }

    // --- Clinical Prescriptions ---
    @Transactional
    public ClinicalPrescription addPrescription(ClinicalPrescription prescription, Long patientId, Long doctorId, Long visitId) {
        prescription.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        prescription.setDoctor(doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found")));
        if (visitId != null) {
            prescription.setVisit(opdVisitRepository.findById(visitId).orElseThrow(() -> new RuntimeException("Visit not found")));
        }
        prescription.setPrescriptionTime(LocalDateTime.now());
        prescription.setHospitalId(TenantContext.getHospitalId());
        prescription.setBranchId(TenantContext.getBranchId());
        return clinicalPrescriptionRepository.save(prescription);
    }

    public Page<ClinicalPrescription> getPrescriptionsByPatient(Long patientId, Pageable pageable) {
        return clinicalPrescriptionRepository.findByPatientId(patientId, pageable);
    }

    public Page<ClinicalPrescription> getPrescriptionsByVisit(Long visitId, Pageable pageable) {
        return clinicalPrescriptionRepository.findByVisitId(visitId, pageable);
    }
}
