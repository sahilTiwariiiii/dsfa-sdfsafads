package com.example.samrat.modules.appointment.repository;

import com.example.samrat.modules.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.hospitalId = :hospitalId AND a.branchId = :branchId")
    long countByDoctorIdAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date, @Param("hospitalId") Long hospitalId, @Param("branchId") Long branchId);

    List<Appointment> findByHospitalIdAndBranchIdAndAppointmentDate(Long hospitalId, Long branchId, LocalDate date);

    List<Appointment> findByPatientId(Long patientId);
}
