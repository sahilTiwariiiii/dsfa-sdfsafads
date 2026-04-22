package com.example.samrat.modules.reporting.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.reporting.entity.SystemReport;
import com.example.samrat.modules.reporting.repository.SystemReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final SystemReportRepository reportRepository;

    @Transactional
    public SystemReport generateReport(String name, String type, String format, String generatedBy) {
        SystemReport report = new SystemReport();
        report.setReportName(name);
        report.setReportType(type);
        report.setReportFormat(format);
        report.setGeneratedBy(generatedBy);
        report.setGeneratedAt(LocalDateTime.now());
        report.setReportUrl("http://storage.hms.com/reports/" + name.toLowerCase().replace(" ", "_") + "." + format.toLowerCase());
        report.setHospitalId(TenantContext.getHospitalId());
        report.setBranchId(TenantContext.getBranchId());

        return reportRepository.save(report);
    }

    public List<SystemReport> getReports() {
        return reportRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId());
    }
}
