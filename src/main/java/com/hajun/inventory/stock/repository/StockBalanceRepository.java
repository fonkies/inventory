package com.hajun.inventory.stock.repository;

import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.stock.domain.StockBalance;
import com.hajun.inventory.warehouse.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockBalanceRepository extends JpaRepository<StockBalance, Long> {
    Optional<StockBalance> findByItemAndWarehouse(Item item, Warehouse warehouse);
}
