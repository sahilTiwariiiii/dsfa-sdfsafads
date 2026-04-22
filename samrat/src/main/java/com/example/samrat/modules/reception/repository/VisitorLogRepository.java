package com.example.samrat.modules.reception.repository;

import com.example.samrat.modules.reception.entity.VisitorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorLogRepository extends JpaRepository<VisitorLog, Long> {
    List<VisitorLog> findByExitTimeIsNull(); // Currently in hospital
}
