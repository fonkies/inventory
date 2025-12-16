package com.hajun.inventory.item.dto;

import com.hajun.inventory.item.domain.Item;

public record ItemResponse(
        Long id,
        String code,
        String name,
        boolean active
) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getCode(),
                item.getName(),
                item.isActive()
        );
    }
}
