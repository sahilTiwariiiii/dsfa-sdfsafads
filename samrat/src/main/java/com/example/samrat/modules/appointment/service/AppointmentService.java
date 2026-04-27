package com.example.samrat.modules.appointment.service;

import com.example.samrat.modules.admin.entity.Department;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import com.example.samrat.modules.appointment.dto.AppointmentDTO;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.repository.AppointmentRepository;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import com.example.samrat.core.context.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private static final Set<String> ALLOWED_VISIT_TYPES = Set.of("OPD", "IPD", "EMERGENCY");

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public AppointmentDTO createAppointment(Long patientId, Long doctorId, Long departmentId, LocalDate appointmentDate, String visitType, String priority, String source, String notes) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Department department = null;
        if (departmentId != null) {
            department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));
        }

        // Generate token number: DoctorInitials-Date-Count
        long count = appointmentRepository.countByDoctorIdAndDate(
                doctorId, appointmentDate, TenantContext.getHospitalId(), TenantContext.getBranchId());

        String doctorInitials = doctor.getUser().getFullName().substring(0, 2).toUpperCase();
        String token = String.format("%s-%d", doctorInitials, count + 1);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(java.time.LocalTime.now());
        appointment.setTokenNumber(token);
        appointment.setVisitType(normalizeAndValidateVisitType(visitType));
        appointment.setPriority(priority != null ? Appointment.AppointmentPriority.valueOf(priority.toUpperCase()) : Appointment.AppointmentPriority.NORMAL);
        appointment.setSource(source);
        appointment.setNotes(notes);
        appointment.setHospitalId(TenantContext.getHospitalId());
        appointment.setBranchId(TenantContext.getBranchId());

        Appointment saved = appointmentRepository.save(appointment);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getDailyAppointments(LocalDate date, Pageable pageable) {
        return appointmentRepository.findByHospitalIdAndBranchIdAndAppointmentDate(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), date, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return toDTO(appointment);
    }

    @Transactional
    public AppointmentDTO updateAppointmentStatus(Long id, String status, String cancellationReason) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(Appointment.AppointmentStatus.valueOf(status.toUpperCase()));
        if (cancellationReason != null) {
            appointment.setCancellationReason(cancellationReason);
        }
        Appointment saved = appointmentRepository.save(appointment);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsByPatient(Long patientId, Pageable pageable) {
        return appointmentRepository.findByPatientId(patientId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsByDoctor(Long doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorId(doctorId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> searchAppointments(Long patientId, Long doctorId, Long departmentId, LocalDate startDate, LocalDate endDate, String status, Pageable pageable) {
        Appointment.AppointmentStatus appStatus = status != null ? Appointment.AppointmentStatus.valueOf(status.toUpperCase()) : null;
        return appointmentRepository.searchAppointments(
                TenantContext.getHospitalId(), TenantContext.getBranchId(),
                patientId, doctorId, departmentId, startDate, endDate, appStatus, pageable).map(this::toDTO);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private String normalizeAndValidateVisitType(String visitType) {
        if (visitType == null || visitType.isBlank()) {
            throw new RuntimeException("Visit type is required. Allowed values: OPD, IPD, EMERGENCY");
        }

        String normalized = visitType.trim().toUpperCase();
        if (!ALLOWED_VISIT_TYPES.contains(normalized)) {
            throw new RuntimeException("Invalid visit type: " + visitType + ". Allowed values: OPD, IPD, EMERGENCY");
        }
        return normalized;
    }

    private AppointmentDTO toDTO(Appointment a) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(a.getId());
        if (a.getPatient() != null) {
            dto.setPatientId(a.getPatient().getId());
            // avoid depending on a specific patient schema; try common fields safely
            try {
                dto.setPatientName(a.getPatient().getFullName());
            } catch (Exception ignored) {
                // fallback: keep null if not available
            }
        }
        if (a.getDoctor() != null) {
            dto.setDoctorId(a.getDoctor().getId());
            if (a.getDoctor().getUser() != null) {
                dto.setDoctorName(a.getDoctor().getUser().getFullName());
            }
        }
        if (a.getDepartment() != null) {
            dto.setDepartmentId(a.getDepartment().getId());
            dto.setDepartmentName(a.getDepartment().getName());
        }
        dto.setAppointmentDate(a.getAppointmentDate());
        dto.setAppointmentTime(a.getAppointmentTime());
        dto.setTokenNumber(a.getTokenNumber());
        dto.setStatus(a.getStatus());
        dto.setPriority(a.getPriority());
        dto.setVisitType(a.getVisitType());
        dto.setSource(a.getSource());
        dto.setBilled(a.isBilled());
        dto.setNotes(a.getNotes());
        dto.setCancellationReason(a.getCancellationReason());
        return dto;
    }
}
