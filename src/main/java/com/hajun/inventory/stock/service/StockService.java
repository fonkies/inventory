package com.hajun.inventory.stock.service;

import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.warehouse.domain.Warehouse;
import com.hajun.inventory.common.exception.ConflictException;
import com.hajun.inventory.common.exception.NotFoundException; // 이것도 같은 폴더면 같이
import com.hajun.inventory.item.repository.ItemRepository;
import com.hajun.inventory.stock.domain.*;
import com.hajun.inventory.stock.dto.StockMovementResponse;
import com.hajun.inventory.stock.repository.StockBalanceRepository;
import com.hajun.inventory.stock.repository.StockMovementRepository;
import com.hajun.inventory.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hajun.inventory.stock.dto.StockRequest;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final ItemRepository itemRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockBalanceRepository stockBalanceRepository;
    private final StockMovementRepository stockMovementRepository;

    public StockMovementResponse stockIn(StockRequest req) {
        Item item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found: " + req.itemId()));

        Warehouse warehouse = warehouseRepository.findById(req.warehouseId())
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found: " + req.warehouseId()));

        StockBalance balance = stockBalanceRepository
                .findByItem_IdAndWarehouse_Id(req.itemId(), req.warehouseId())
                .orElseGet(() -> StockBalance.create(item, warehouse)); // 여기 중요

        balance.increase(req.quantity());
        stockBalanceRepository.save(balance);

        StockMovement movement = StockMovement.of(item, warehouse, StockMovementType.IN, req.quantity().longValue());
        stockMovementRepository.save(movement);

        return StockMovementResponse.from(movement);
    }


    public StockMovementResponse stockOut(StockRequest req) {
        Item item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found: " + req.itemId()));

        Warehouse warehouse = warehouseRepository.findById(req.warehouseId())
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found: " + req.warehouseId()));

        StockBalance balance = stockBalanceRepository
                .findByItem_IdAndWarehouse_Id(req.itemId(), req.warehouseId())
                .orElseThrow(() -> new ConflictException("STOCK_INSUFFICIENT", "Insufficient stock."));

        long qty = req.quantity().longValue();
        if (balance.getQuantity() < qty) {
            throw new ConflictException("STOCK_INSUFFICIENT", "Insufficient stock.");
        }

        balance.decrease(qty);

        StockMovement movement = StockMovement.of(item, warehouse, StockMovementType.OUT, qty);
        stockMovementRepository.save(movement);

        return StockMovementResponse.from(movement);
    }


    @Transactional(readOnly = true)
    public List<StockMovementResponse> movements(Long itemId, Long warehouseId) {
        return stockMovementRepository
                .findByItemIdAndWarehouseIdOrderByIdDesc(itemId, warehouseId)
                .stream()
                .map(StockMovementResponse::from)
                .toList();
    }

    private void ensureExists(Long itemId, Long warehouseId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found: " + itemId));
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found: " + warehouseId));
    }
}
