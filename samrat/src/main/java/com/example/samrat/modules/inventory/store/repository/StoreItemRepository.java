package com.example.samrat.modules.inventory.store.repository;

import com.example.samrat.modules.inventory.store.entity.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {
    Optional<StoreItem> findByItemCode(String itemCode);
}
