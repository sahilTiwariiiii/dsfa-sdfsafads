package com.example.samrat.modules.dashboard.dto;

import lombok.Data;
import java.util.List;

@Data
public class DoctorDashboardDTO {
    private Long todayAppointmentsCount;
    private Long pendingSurgeriesCount;
    private Long admittedPatientsCount;
    private List<?> upcomingAppointments;
    private List<?> recentLabResults;
}
