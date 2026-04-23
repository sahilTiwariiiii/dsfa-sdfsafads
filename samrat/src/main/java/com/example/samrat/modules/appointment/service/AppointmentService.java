package com.example.samrat.modules.appointment.service;

import com.example.samrat.modules.admin.entity.Department;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
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

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Appointment createAppointment(Long patientId, Long doctorId, Long departmentId, LocalDate appointmentDate, String visitType, String priority, String source, String notes) {
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
        appointment.setVisitType(visitType);
        appointment.setPriority(priority != null ? Appointment.AppointmentPriority.valueOf(priority.toUpperCase()) : Appointment.AppointmentPriority.NORMAL);
        appointment.setSource(source);
        appointment.setNotes(notes);
        appointment.setHospitalId(TenantContext.getHospitalId());
        appointment.setBranchId(TenantContext.getBranchId());

        return appointmentRepository.save(appointment);
    }

    public Page<Appointment> getDailyAppointments(LocalDate date, Pageable pageable) {
        return appointmentRepository.findByHospitalIdAndBranchIdAndAppointmentDate(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), date, pageable);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long id, String status, String cancellationReason) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Appointment.AppointmentStatus.valueOf(status.toUpperCase()));
        if (cancellationReason != null) {
            appointment.setCancellationReason(cancellationReason);
        }
        return appointmentRepository.save(appointment);
    }

    public Page<Appointment> getAppointmentsByPatient(Long patientId, Pageable pageable) {
        return appointmentRepository.findByPatientId(patientId, pageable);
    }

    public Page<Appointment> getAppointmentsByDoctor(Long doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorId(doctorId, pageable);
    }

    public Page<Appointment> searchAppointments(Long patientId, Long doctorId, Long departmentId, LocalDate startDate, LocalDate endDate, String status, Pageable pageable) {
        Appointment.AppointmentStatus appStatus = status != null ? Appointment.AppointmentStatus.valueOf(status.toUpperCase()) : null;
        return appointmentRepository.searchAppointments(
                TenantContext.getHospitalId(), TenantContext.getBranchId(),
                patientId, doctorId, departmentId, startDate, endDate, appStatus, pageable);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
