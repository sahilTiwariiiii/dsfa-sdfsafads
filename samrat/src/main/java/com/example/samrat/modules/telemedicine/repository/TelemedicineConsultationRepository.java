package com.example.samrat.modules.telemedicine.repository;

import com.example.samrat.modules.telemedicine.entity.TelemedicineConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TelemedicineConsultationRepository extends JpaRepository<TelemedicineConsultation, Long> {
    Optional<TelemedicineConsultation> findByAppointmentId(Long appointmentId);
}
