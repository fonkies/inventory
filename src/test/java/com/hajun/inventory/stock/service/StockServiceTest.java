package com.hajun.inventory.stock.service;

import com.hajun.inventory.common.exception.ConflictException;
import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.item.repository.ItemRepository;
import com.hajun.inventory.stock.dto.StockRequest;
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

    @Autowired StockService stockService;
    @Autowired ItemRepository itemRepository;
    @Autowired WarehouseRepository warehouseRepository;
    @Autowired StockBalanceRepository stockBalanceRepository;

    private Item item;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        // TODO: 여기 2줄은 너 Item/Warehouse 생성 방식에 맞게만 바꿔주면 끝.
        item = itemRepository.save(createItem());
        warehouse = warehouseRepository.save(createWarehouse());
    }

    @Test
    void stockIn_increases_balance_quantity() {
        // given
        StockRequest req = new StockRequest(item.getId(), warehouse.getId(), 10);

        // when
        stockService.stockIn(req);

        // then
        var balance = stockBalanceRepository
                .findByItem_IdAndWarehouse_Id(item.getId(), warehouse.getId())
                .orElseThrow();

        assertEquals(10L, balance.getQuantity());
    }

    @Test
    void stockOut_throws_conflict_when_insufficient_stock() {
        // given: 재고 5
        stockService.stockIn(new StockRequest(item.getId(), warehouse.getId(), 5));

        // when & then: 10 출고 시도 -> 실패
        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> stockService.stockOut(new StockRequest(item.getId(), warehouse.getId(), 10))
        );

        assertEquals("STOCK_INSUFFICIENT", ex.getCode());
    }

    // ------------------------
    // 아래 2개는 네 엔티티 필드에 맞게 '타이핑'으로 최소 수정
    // ------------------------
    private Item createItem() {
        // 예시1) Item.of(...)가 있으면 그걸로
        // return Item.of("ITEM-001", "연필");

        // 예시2) 빌더가 있으면 빌더로
        // return Item.builder().code("ITEM-001").name("연필").build();

        // 일단 컴파일 안 되면 너 Item 엔티티 필드/생성 방법 알려줘. 딱 맞춰줄게.
        return Item.create("WH-001", "부산창고");
    }

    private Warehouse createWarehouse() {
        // 예시1) Warehouse.of(...)가 있으면 그걸로
        // return Warehouse.of("WH-001", "부산창고");

        // 예시2) 빌더가 있으면 빌더로
        // return Warehouse.builder().code("WH-001").name("부산창고").build();

        return Warehouse.create("WH-001", "부산창고");
    }
}
