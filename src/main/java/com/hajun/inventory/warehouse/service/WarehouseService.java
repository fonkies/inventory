package com.hajun.inventory.warehouse.service;

import com.hajun.inventory.common.exception.ConflictException;
import com.hajun.inventory.common.exception.NotFoundException;
import com.hajun.inventory.warehouse.domain.Warehouse;
import com.hajun.inventory.warehouse.dto.WarehouseCreateRequest;
import com.hajun.inventory.warehouse.dto.WarehouseResponse;
import com.hajun.inventory.warehouse.dto.WarehouseUpdateRequest;
import com.hajun.inventory.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Transactional
    public WarehouseResponse create(WarehouseCreateRequest request) {
        if (warehouseRepository.existsByCode(request.code())) {
            throw new ConflictException("WAREHOUSE_CODE_DUPLICATE", "Warehouse code already exists: " + request.code());
        }

        Warehouse warehouse = Warehouse.create(request.code(), request.name());
        Warehouse saved = warehouseRepository.save(warehouse);
        return WarehouseResponse.from(saved);
    }

    public WarehouseResponse get(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found. id=" + id));
        return WarehouseResponse.from(warehouse);
    }

    public List<WarehouseResponse> list() {
        return warehouseRepository.findAll().stream()
                .map(WarehouseResponse::from)
                .toList();
    }

    @Transactional
    public WarehouseResponse update(Long id, WarehouseUpdateRequest request) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found. id=" + id));

        if (!warehouse.getCode().equals(request.code()) && warehouseRepository.existsByCode(request.code())) {
            throw new ConflictException("WAREHOUSE_CODE_DUPLICATE", "Warehouse code already exists: " + request.code());
        }

        warehouse.update(request.code(), request.name(), warehouse.isActive());
        return WarehouseResponse.from(warehouse);
    }

    @Transactional
    public void delete(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("WAREHOUSE_NOT_FOUND", "Warehouse not found. id=" + id));
        warehouseRepository.delete(warehouse);
    }
}
