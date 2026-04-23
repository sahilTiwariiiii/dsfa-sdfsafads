package com.example.samrat.modules.asset.repository;

import com.example.samrat.modules.asset.entity.AssetSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetSubCategoryRepository extends JpaRepository<AssetSubCategory, Long> {
    List<AssetSubCategory> findByCategoryId(Long categoryId);
}
