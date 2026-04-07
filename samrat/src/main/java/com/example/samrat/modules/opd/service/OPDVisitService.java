package com.example.samrat.modules.opd.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.repository.AppointmentRepository;
import com.example.samrat.modules.opd.entity.OPDVisit;
import com.example.samrat.modules.opd.repository.OPDVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OPDVisitService {

    private final OPDVisitRepository opdVisitRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public OPDVisit checkIn(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        OPDVisit opdVisit = new OPDVisit();
        opdVisit.setAppointment(appointment);
        opdVisit.setPatient(appointment.getPatient());
        opdVisit.setDoctor(appointment.getDoctor());
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
    public OPDVisit recordVitals(Long opdVisitId, Double weight, Double height, Double bp, Double temp, Double pulse) {
        OPDVisit opdVisit = opdVisitRepository.findById(opdVisitId)
                .orElseThrow(() -> new RuntimeException("OPD Visit not found"));

        opdVisit.setWeight(weight);
        opdVisit.setHeight(height);
        opdVisit.setBloodPressure(bp);
        opdVisit.setTemperature(temp);
        opdVisit.setPulseRate(pulse);

        return opdVisitRepository.save(opdVisit);
    }
}
