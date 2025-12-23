package com.hajun.inventory.stock.service;

import com.hajun.inventory.global.exception.ConflictException;
import com.hajun.inventory.common.exception.NotFoundException;
import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.item.repository.ItemRepository;
import com.hajun.inventory.stock.domain.StockBalance;
import com.hajun.inventory.stock.domain.StockMovement;
import com.hajun.inventory.stock.domain.StockMovementType;
import com.hajun.inventory.stock.repository.StockBalanceRepository;
import com.hajun.inventory.stock.repository.StockMovementRepository;
import com.hajun.inventory.warehouse.domain.Warehouse;
import com.hajun.inventory.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final ItemRepository itemRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockBalanceRepository stockBalanceRepository;
    private final StockMovementRepository stockMovementRepository;

    @Transactional(readOnly = true)
    public List<StockMovement> movements(Long itemId, Long warehouseId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found: " + itemId));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found: " + warehouseId));

        return stockMovementRepository.findTop50ByItemAndWarehouseOrderByOccurredAtDesc(item, warehouse);
    }

    public void stockIn(Long itemId, Long warehouseId, long qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be positive");

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found: " + itemId));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found: " + warehouseId));

        StockBalance balance = stockBalanceRepository.findByItemAndWarehouse(item, warehouse)
                .orElseGet(() -> stockBalanceRepository.save(StockBalance.create(item, warehouse)));

        balance.increase(qty);

        // A안: 변화량 저장 (+qty)
        stockMovementRepository.save(
                StockMovement.of(item, warehouse, StockMovementType.IN, qty)
        );
    }

    public void stockOut(Long itemId, Long warehouseId, long qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be positive");

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found: " + itemId));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found: " + warehouseId));

        StockBalance balance = stockBalanceRepository.findByItemAndWarehouse(item, warehouse)
                .orElseGet(() -> stockBalanceRepository.save(StockBalance.create(item, warehouse)));

        try {
            balance.decrease(qty);
        } catch (IllegalStateException e) {
            throw new ConflictException("INSUFFICIENT_STOCK", "Insufficient stock.");
        }

        // A안: 변화량 저장 (-qty)
        stockMovementRepository.save(
                StockMovement.of(item, warehouse, StockMovementType.OUT, -qty)
        );
    }

    public void adjust(Long itemId, Long warehouseId, long deltaQty) {
        if (deltaQty == 0) throw new IllegalArgumentException("deltaQty must not be zero");

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found: " + itemId));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found: " + warehouseId));

        StockBalance balance = stockBalanceRepository.findByItemAndWarehouse(item, warehouse)
                .orElseGet(() -> stockBalanceRepository.save(StockBalance.create(item, warehouse)));

        if (deltaQty > 0) {
            balance.increase(deltaQty);
        } else {
            long abs = Math.abs(deltaQty);
            try {
                balance.decrease(abs);
            } catch (IllegalStateException e) {
                throw new ConflictException("INSUFFICIENT_STOCK", "Insufficient stock.");
            }
        }

        // A안: deltaQty 그대로 저장 (+/-)
        stockMovementRepository.save(
                StockMovement.of(item, warehouse, StockMovementType.ADJUST, deltaQty)
        );
    }
}
