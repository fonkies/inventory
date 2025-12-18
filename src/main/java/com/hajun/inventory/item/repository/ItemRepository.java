package com.hajun.inventory.item.repository;

import com.hajun.inventory.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hajun.inventory.item.repository.ItemRepository;
import com.hajun.inventory.warehouse.repository.WarehouseRepository;


import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByCode(String code);
    boolean existsByCode(String code);
}
