package com.hajun.inventory.warehouse.controller;

import com.hajun.inventory.warehouse.dto.WarehouseCreateRequest;
import com.hajun.inventory.warehouse.dto.WarehouseResponse;
import com.hajun.inventory.warehouse.dto.WarehouseUpdateRequest;
import com.hajun.inventory.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<WarehouseResponse> create(@RequestBody WarehouseCreateRequest request) {
        WarehouseResponse created = warehouseService.create(request);
        return ResponseEntity
                .created(URI.create("/api/warehouses/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public WarehouseResponse get(@PathVariable Long id) {
        return warehouseService.get(id);
    }

    @GetMapping
    public List<WarehouseResponse> list() {
        return warehouseService.list();
    }

    @PutMapping("/{id}")
    public WarehouseResponse update(@PathVariable Long id, @RequestBody WarehouseUpdateRequest request) {
        return warehouseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        warehouseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
