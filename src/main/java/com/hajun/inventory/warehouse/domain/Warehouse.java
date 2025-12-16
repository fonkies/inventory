package com.hajun.inventory.warehouse.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "warehouses",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_warehouses_code", columnNames = "code")
        }
)
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    protected Warehouse() { }

    public Warehouse(String code, String name) {
        this.code = code;
        this.name = name;
        this.active = true;
    }

    public static Warehouse create(String code, String name) {
        return new Warehouse(code, name);
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public boolean isActive() { return active; }

    public void update(String code, String name, boolean active) {
        this.code = code;
        this.name = name;
        this.active = active;
    }
}
