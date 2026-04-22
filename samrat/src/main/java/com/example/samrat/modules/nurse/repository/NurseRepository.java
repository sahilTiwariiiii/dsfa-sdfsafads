package com.example.samrat.modules.nurse.repository;

import com.example.samrat.modules.nurse.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    Optional<Nurse> findByUserId(Long userId);
    List<Nurse> findByDepartmentId(Long departmentId);
    List<Nurse> findByAvailableTrue();
}
