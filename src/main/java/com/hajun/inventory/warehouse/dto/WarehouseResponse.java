package com.hajun.inventory.warehouse.dto;

import com.hajun.inventory.warehouse.domain.Warehouse;

public record WarehouseResponse(
        Long id,
        String code,
        String name,
        boolean active
) {
    public static WarehouseResponse from(Warehouse warehouse) {
        return new WarehouseResponse(
                warehouse.getId(),
                warehouse.getCode(),
                warehouse.getName(),
                warehouse.isActive()
        );
    }
}
