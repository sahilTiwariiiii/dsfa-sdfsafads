package com.example.samrat.modules.finance.insurance.repository;

import com.example.samrat.modules.finance.insurance.entity.TPACompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TPACompanyRepository extends JpaRepository<TPACompany, Long> {
    Optional<TPACompany> findByCode(String code);
}
