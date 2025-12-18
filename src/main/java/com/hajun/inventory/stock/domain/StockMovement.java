package com.hajun.inventory.stock.domain;

import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.warehouse.domain.Warehouse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StockMovementType type;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    public static StockMovement of(
            Item item,
            Warehouse warehouse,
            StockMovementType type,
            long quantity
    ) {
        StockMovement m = new StockMovement();
        m.item = item;
        m.warehouse = warehouse;
        m.type = type;
        m.quantity = quantity;
        m.occurredAt = LocalDateTime.now();
        return m;
    }
}
