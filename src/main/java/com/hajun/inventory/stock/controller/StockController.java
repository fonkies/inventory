package com.hajun.inventory.stock.controller;

import com.hajun.inventory.stock.dto.StockMovementResponse;
import com.hajun.inventory.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.hajun.inventory.stock.dto.StockRequest;


import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/in")
    public void stockIn(@RequestBody StockRequest req) {
        stockService.stockIn(req);
    }

    @PostMapping("/out")
    public void stockOut(@RequestBody StockRequest req) {
        stockService.stockOut(req);
    }

    @GetMapping("/movement")
    public List<StockMovementResponse> movements(
            @RequestParam Long itemId,
            @RequestParam Long warehouseId
    ) {
        return stockService.movements(itemId, warehouseId);
    }
}
