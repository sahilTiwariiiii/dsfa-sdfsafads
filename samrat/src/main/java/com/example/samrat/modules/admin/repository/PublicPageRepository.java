package com.example.samrat.modules.admin.repository;

import com.example.samrat.modules.admin.entity.PublicPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PublicPageRepository extends JpaRepository<PublicPage, Long> {
    Optional<PublicPage> findBySlug(String slug);
}
