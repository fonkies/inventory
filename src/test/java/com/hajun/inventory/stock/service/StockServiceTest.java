package com.hajun.inventory.stock.service;

import com.hajun.inventory.global.exception.ConflictException;
import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.item.repository.ItemRepository;
import com.hajun.inventory.stock.repository.StockBalanceRepository;
import com.hajun.inventory.warehouse.domain.Warehouse;
import com.hajun.inventory.warehouse.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StockServiceTest {

    @Autowired
    StockService stockService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    StockBalanceRepository stockBalanceRepository;

    private Item item;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        item = itemRepository.save(createItem());
        warehouse = warehouseRepository.save(createWarehouse());
    }

    @Test
    void stockIn_increases_balance_quantity() {
        // when
        stockService.stockIn(item.getId(), warehouse.getId(), 10);

        // then
        var balance = stockBalanceRepository
                .findByItemAndWarehouse(item, warehouse)
                .orElseThrow();

        assertEquals(10L, balance.getQuantity());
    }

    @Test
    void stockOut_throws_conflict_when_insufficient_stock() {
        // given: 재고 5
        stockService.stockIn(item.getId(), warehouse.getId(), 5);

        // when & then: 10 출고 시도 -> 실패
        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> stockService.stockOut(item.getId(), warehouse.getId(), 10)
        );

        // 네가 서비스에서 던지는 코드에 맞춰라.
        // (우리가 맞춘 건 "INSUFFICIENT_STOCK")
        assertEquals("INSUFFICIENT_STOCK", ex.getCode());
    }

    private Item createItem() {
        return Item.create("ITEM-001", "연필");
    }

    private Warehouse createWarehouse() {
        return Warehouse.create("WH-001", "부산창고");
    }
}
