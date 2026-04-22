package com.example.samrat.modules.dashboard.dto;

import lombok.Data;
import java.util.List;

@Data
public class PatientPortalDTO {
    private String uhid;
    private String fullName;
    private List<?> myAppointments;
    private List<?> myPrescriptions;
    private List<?> myLabReports;
    private Double outstandingBalance;
}
