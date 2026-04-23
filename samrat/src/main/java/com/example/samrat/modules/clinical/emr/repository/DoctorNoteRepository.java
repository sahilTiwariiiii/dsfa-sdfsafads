package com.example.samrat.modules.clinical.emr.repository;

import com.example.samrat.modules.clinical.emr.entity.DoctorNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorNoteRepository extends JpaRepository<DoctorNote, Long> {
    Page<DoctorNote> findByPatientId(Long patientId, Pageable pageable);
}
