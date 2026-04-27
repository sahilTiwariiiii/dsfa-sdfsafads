package com.example.samrat.modules.opd.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.repository.AppointmentRepository;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.opd.dto.OPDVisitDTO;
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
    public OPDVisitDTO checkIn(Long appointmentId) {
        Long hospitalId = TenantContext.getHospitalId();
        Long branchId = TenantContext.getBranchId();

        OPDVisit existingVisit = opdVisitRepository
                .findByHospitalIdAndBranchIdAndAppointmentId(hospitalId, branchId, appointmentId)
                .orElse(null);
        if (existingVisit != null) {
            throw new RuntimeException("Patient already checked in for this appointment");
        }

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
        opdVisit.setHospitalId(hospitalId);
        opdVisit.setBranchId(branchId);

        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);

        return toDTO(opdVisitRepository.save(opdVisit));
    }

    @Transactional
    public OPDVisitDTO registerWalkInVisit(Long patientId, Long doctorId, Long departmentId, String visitType, String slot, Double fee) {
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

        return toDTO(opdVisitRepository.save(opdVisit));
    }

    @Transactional(readOnly = true)
    public Page<OPDVisitDTO> getAllVisits(Pageable pageable) {
        return opdVisitRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<OPDVisitDTO> searchVisits(Long patientId, Long doctorId, Long departmentId, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        OPDVisit.VisitStatus visitStatus = status != null ? OPDVisit.VisitStatus.valueOf(status.toUpperCase()) : null;
        return opdVisitRepository.searchVisits(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, doctorId, departmentId, visitStatus, start, end, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public OPDVisitDTO getVisitById(Long id) {
        return toDTO(getVisitEntityById(id));
    }

    private OPDVisit getVisitEntityById(Long id) {
        return opdVisitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OPD Visit not found"));
    }

    @Transactional
    public OPDVisitDTO recordVitals(Long opdVisitId, Double weight, Double height, String bp, Double temp, Integer pulse, Integer resp, Integer spo2) {
        OPDVisit opdVisit = getVisitEntityById(opdVisitId);
        opdVisit.setWeight(weight);
        opdVisit.setHeight(height);
        opdVisit.setBloodPressure(bp);
        opdVisit.setTemperature(temp);
        opdVisit.setPulseRate(pulse);
        opdVisit.setRespiratoryRate(resp);
        opdVisit.setSpo2(spo2);
        return toDTO(opdVisitRepository.save(opdVisit));
    }

    @Transactional
    public OPDVisitDTO updateVisitStatus(Long id, String status, String remark) {
        OPDVisit opdVisit = getVisitEntityById(id);
        opdVisit.setStatus(OPDVisit.VisitStatus.valueOf(status.toUpperCase()));
        if (remark != null) {
            opdVisit.setRemark(remark);
        }
        return toDTO(opdVisitRepository.save(opdVisit));
    }

    @Transactional
    public void deleteVisit(Long id) {
        opdVisitRepository.deleteById(id);
    }

    private OPDVisitDTO toDTO(OPDVisit visit) {
        OPDVisitDTO dto = new OPDVisitDTO();
        dto.setId(visit.getId());

        if (visit.getAppointment() != null) {
            dto.setAppointmentId(visit.getAppointment().getId());
        }
        if (visit.getPatient() != null) {
            dto.setPatientId(visit.getPatient().getId());
            dto.setPatientName(visit.getPatient().getFullName());
        }
        if (visit.getDoctor() != null) {
            dto.setDoctorId(visit.getDoctor().getId());
            if (visit.getDoctor().getUser() != null) {
                dto.setDoctorName(visit.getDoctor().getUser().getFullName());
            }
        }
        if (visit.getDepartment() != null) {
            dto.setDepartmentId(visit.getDepartment().getId());
            dto.setDepartmentName(visit.getDepartment().getName());
        }

        dto.setVisitTime(visit.getVisitTime());
        dto.setTokenNumber(visit.getTokenNumber());
        dto.setStatus(visit.getStatus());
        dto.setVisitType(visit.getVisitType());
        dto.setSlot(visit.getSlot());
        dto.setFee(visit.getFee());
        dto.setPaymentMode(visit.getPaymentMode());
        dto.setDiscountPercent(visit.getDiscountPercent());
        dto.setWeight(visit.getWeight());
        dto.setHeight(visit.getHeight());
        dto.setBloodPressure(visit.getBloodPressure());
        dto.setTemperature(visit.getTemperature());
        dto.setPulseRate(visit.getPulseRate());
        dto.setRespiratoryRate(visit.getRespiratoryRate());
        dto.setSpo2(visit.getSpo2());
        dto.setSource(visit.getSource());
        dto.setRemark(visit.getRemark());
        dto.setNotes(visit.getNotes());
        return dto;
    }
}
