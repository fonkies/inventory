package com.hajun.inventory.stock.dto;

import jakarta.validation.constraints.NotNull;

public record StockRequest(
        @NotNull Long itemId,
        @NotNull Long warehouseId,
        long quantity
) {}
