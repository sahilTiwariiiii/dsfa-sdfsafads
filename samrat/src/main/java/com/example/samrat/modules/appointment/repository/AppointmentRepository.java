package com.example.samrat.modules.appointment.repository;

import com.example.samrat.modules.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.hospitalId = :hospitalId AND a.branchId = :branchId")
    long countByDoctorIdAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date, @Param("hospitalId") Long hospitalId, @Param("branchId") Long branchId);

    List<Appointment> findByHospitalIdAndBranchIdAndAppointmentDate(Long hospitalId, Long branchId, LocalDate date);
    Page<Appointment> findByHospitalIdAndBranchIdAndAppointmentDate(Long hospitalId, Long branchId, LocalDate date, Pageable pageable);

    List<Appointment> findByPatientId(Long patientId);
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    List<Appointment> findByDoctorId(Long doctorId);
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);

    List<Appointment> findByDepartmentId(Long departmentId);
    Page<Appointment> findByDepartmentId(Long departmentId, Pageable pageable);

    List<Appointment> findByStatus(Appointment.AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.hospitalId = :hospitalId AND a.branchId = :branchId " +
           "AND (:patientId IS NULL OR a.patient.id = :patientId) " +
           "AND (:doctorId IS NULL OR a.doctor.id = :doctorId) " +
           "AND (:departmentId IS NULL OR a.department.id = :departmentId) " +
           "AND (:startDate IS NULL OR a.appointmentDate >= :startDate) " +
           "AND (:endDate IS NULL OR a.appointmentDate <= :endDate) " +
           "AND (:status IS NULL OR a.status = :status)")
    Page<Appointment> searchAppointments(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("departmentId") Long departmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") Appointment.AppointmentStatus status,
            Pageable pageable);
}
