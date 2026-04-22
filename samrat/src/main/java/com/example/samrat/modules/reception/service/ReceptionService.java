package com.example.samrat.modules.reception.service;

import com.example.samrat.modules.reception.entity.VisitorLog;
import com.example.samrat.modules.reception.repository.VisitorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceptionService {

    private final VisitorLogRepository visitorLogRepository;

    public VisitorLog logEntry(VisitorLog visitorLog) {
        visitorLog.setEntryTime(LocalDateTime.now());
        return visitorLogRepository.save(visitorLog);
    }

    public VisitorLog logExit(Long id) {
        VisitorLog log = visitorLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));
        log.setExitTime(LocalDateTime.now());
        return visitorLogRepository.save(log);
    }

    public List<VisitorLog> getActiveVisitors() {
        return visitorLogRepository.findByExitTimeIsNull();
    }
}
