package com.hajun.inventory.stock.dto;

import com.hajun.inventory.stock.domain.StockMovement;
import com.hajun.inventory.stock.domain.StockMovementType;

import java.time.LocalDateTime;

public record StockMovementResponse(
        Long id,
        Long itemId,
        Long warehouseId,
        StockMovementType type,
        long quantity,
        LocalDateTime occurredAt
) {
    public static StockMovementResponse from(StockMovement m) {
        return new StockMovementResponse(
                m.getId(),
                m.getItem().getId(),
                m.getWarehouse().getId(),
                m.getType(),
                m.getQuantity(),
                m.getOccurredAt()
        );
    }
}
