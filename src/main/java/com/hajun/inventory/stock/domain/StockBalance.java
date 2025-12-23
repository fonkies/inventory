package com.hajun.inventory.stock.domain;

import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.warehouse.domain.Warehouse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "stock_balances",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_stock_balance_item_warehouse",
                columnNames = {"item_id", "warehouse_id"}
        )
)
public class StockBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false)
    private long quantity;

    private StockBalance(Item item, Warehouse warehouse, long quantity) {
        this.item = item;
        this.warehouse = warehouse;
        this.quantity = quantity;
    }

    public static StockBalance create(Item item, Warehouse warehouse) {
        return new StockBalance(item, warehouse, 0L);
    }

    public void increase(long qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be positive");
        this.quantity += qty;
    }

    public void decrease(long qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be positive");
        if (this.quantity < qty) throw new IllegalStateException("insufficient stock");
        this.quantity -= qty;
    }
}
