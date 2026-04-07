package com.example.samrat.modules.admin.repository;

import com.example.samrat.modules.admin.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByHospitalId(Long hospitalId);
    Optional<Branch> findByCode(String code);
}
