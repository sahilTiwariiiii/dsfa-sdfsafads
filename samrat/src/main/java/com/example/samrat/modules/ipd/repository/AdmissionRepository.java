package com.example.samrat.modules.ipd.repository;

import com.example.samrat.modules.ipd.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
}
