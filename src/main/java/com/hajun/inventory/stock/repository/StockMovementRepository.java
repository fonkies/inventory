package com.hajun.inventory.stock.repository;

import com.hajun.inventory.stock.domain.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByItemIdAndWarehouseIdOrderByIdDesc(Long itemId, Long warehouseId);
}
