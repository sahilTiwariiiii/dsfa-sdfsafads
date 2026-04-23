package com.example.samrat.modules.opd.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.repository.AppointmentRepository;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.opd.entity.OPDVisit;
import com.example.samrat.modules.opd.repository.OPDVisitRepository;
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
public class OPDVisitService {

    private final OPDVisitRepository opdVisitRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public OPDVisit checkIn(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        OPDVisit opdVisit = new OPDVisit();
        opdVisit.setAppointment(appointment);
        opdVisit.setPatient(appointment.getPatient());
        opdVisit.setDoctor(appointment.getDoctor());
        opdVisit.setDepartment(appointment.getDepartment());
        opdVisit.setVisitTime(LocalDateTime.now());
        opdVisit.setTokenNumber(appointment.getTokenNumber());
        opdVisit.setStatus(OPDVisit.VisitStatus.WAITING);
        opdVisit.setHospitalId(TenantContext.getHospitalId());
        opdVisit.setBranchId(TenantContext.getBranchId());

        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);

        return opdVisitRepository.save(opdVisit);
    }

    @Transactional
    public OPDVisit registerWalkInVisit(Long patientId, Long doctorId, Long departmentId, String visitType, String slot, Double fee) {
        OPDVisit opdVisit = new OPDVisit();
        opdVisit.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        opdVisit.setDoctor(doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found")));
        if (departmentId != null) {
            opdVisit.setDepartment(departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found")));
        }
        opdVisit.setVisitTime(LocalDateTime.now());
        opdVisit.setVisitType(visitType);
        opdVisit.setSlot(slot);
        opdVisit.setFee(fee);
        opdVisit.setTokenNumber("WALK-IN-" + System.currentTimeMillis() % 1000);
        opdVisit.setStatus(OPDVisit.VisitStatus.WAITING);
        opdVisit.setHospitalId(TenantContext.getHospitalId());
        opdVisit.setBranchId(TenantContext.getBranchId());

        return opdVisitRepository.save(opdVisit);
    }

    public Page<OPDVisit> getAllVisits(Pageable pageable) {
        return opdVisitRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), pageable);
    }

    public Page<OPDVisit> searchVisits(Long patientId, Long doctorId, Long departmentId, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        OPDVisit.VisitStatus visitStatus = status != null ? OPDVisit.VisitStatus.valueOf(status.toUpperCase()) : null;
        return opdVisitRepository.searchVisits(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, doctorId, departmentId, visitStatus, start, end, pageable);
    }

    public OPDVisit getVisitById(Long id) {
        return opdVisitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OPD Visit not found"));
    }

    @Transactional
    public OPDVisit recordVitals(Long opdVisitId, Double weight, Double height, String bp, Double temp, Integer pulse, Integer resp, Integer spo2) {
        OPDVisit opdVisit = getVisitById(opdVisitId);
        opdVisit.setWeight(weight);
        opdVisit.setHeight(height);
        opdVisit.setBloodPressure(bp);
        opdVisit.setTemperature(temp);
        opdVisit.setPulseRate(pulse);
        opdVisit.setRespiratoryRate(resp);
        opdVisit.setSpo2(spo2);
        return opdVisitRepository.save(opdVisit);
    }

    @Transactional
    public OPDVisit updateVisitStatus(Long id, String status, String remark) {
        OPDVisit opdVisit = getVisitById(id);
        opdVisit.setStatus(OPDVisit.VisitStatus.valueOf(status.toUpperCase()));
        if (remark != null) {
            opdVisit.setRemark(remark);
        }
        return opdVisitRepository.save(opdVisit);
    }

    @Transactional
    public void deleteVisit(Long id) {
        opdVisitRepository.deleteById(id);
    }
}
