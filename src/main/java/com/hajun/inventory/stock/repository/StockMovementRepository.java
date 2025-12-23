package com.hajun.inventory.stock.repository;

import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.stock.domain.StockMovement;
import com.hajun.inventory.warehouse.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findTop50ByItemAndWarehouseOrderByOccurredAtDesc(Item item, Warehouse warehouse);
}
