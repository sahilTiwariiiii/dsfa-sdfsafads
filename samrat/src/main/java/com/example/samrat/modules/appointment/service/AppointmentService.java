package com.example.samrat.modules.appointment.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.repository.AppointmentRepository;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public Appointment createAppointment(Long patientId, Long doctorId, LocalDate appointmentDate, String visitType) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Generate token number: DoctorInitials-Date-Count
        long count = appointmentRepository.countByDoctorIdAndDate(
                doctorId, appointmentDate, TenantContext.getHospitalId(), TenantContext.getBranchId());

        String doctorInitials = doctor.getUser().getFullName().substring(0, 2).toUpperCase();
        String token = String.format("%s-%d", doctorInitials, count + 1);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(java.time.LocalTime.now()); // simplified
        appointment.setTokenNumber(token);
        appointment.setVisitType(visitType);
        appointment.setHospitalId(TenantContext.getHospitalId());
        appointment.setBranchId(TenantContext.getBranchId());

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getDailyAppointments(LocalDate date) {
        return appointmentRepository.findByHospitalIdAndBranchIdAndAppointmentDate(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), date);
    }
}
