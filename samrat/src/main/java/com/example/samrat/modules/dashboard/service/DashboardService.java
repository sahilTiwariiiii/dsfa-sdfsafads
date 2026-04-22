package com.example.samrat.modules.dashboard.service;

import com.example.samrat.modules.dashboard.dto.DoctorDashboardDTO;
import com.example.samrat.modules.dashboard.dto.PatientPortalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DashboardService {

    public DoctorDashboardDTO getDoctorDashboard(Long doctorId) {
        DoctorDashboardDTO dashboard = new DoctorDashboardDTO();
        // Mock data aggregation logic
        dashboard.setTodayAppointmentsCount(5L);
        dashboard.setPendingSurgeriesCount(2L);
        dashboard.setAdmittedPatientsCount(10L);
        dashboard.setUpcomingAppointments(new ArrayList<>());
        dashboard.setRecentLabResults(new ArrayList<>());
        return dashboard;
    }

    public PatientPortalDTO getPatientPortalData(String uhid) {
        PatientPortalDTO portal = new PatientPortalDTO();
        // Mock data aggregation logic
        portal.setUhid(uhid);
        portal.setFullName("John Doe");
        portal.setMyAppointments(new ArrayList<>());
        portal.setMyPrescriptions(new ArrayList<>());
        portal.setMyLabReports(new ArrayList<>());
        portal.setOutstandingBalance(150.0);
        return portal;
    }
}
