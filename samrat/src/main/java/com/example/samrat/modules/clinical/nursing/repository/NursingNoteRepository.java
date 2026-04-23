package com.example.samrat.modules.clinical.nursing.repository;

import com.example.samrat.modules.clinical.nursing.entity.NursingNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NursingNoteRepository extends JpaRepository<NursingNote, Long> {
    List<NursingNote> findByAdmissionId(Long admissionId);
    Page<NursingNote> findByAdmissionId(Long admissionId, Pageable pageable);
    Page<NursingNote> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);
}
