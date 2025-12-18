package com.hajun.inventory.stock.repository;

import com.hajun.inventory.stock.domain.StockBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockBalanceRepository extends JpaRepository<StockBalance, Long> {

    Optional<StockBalance> findByItem_IdAndWarehouse_Id(Long itemId, Long warehouseId);
}