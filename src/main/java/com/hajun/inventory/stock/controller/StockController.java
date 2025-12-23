package com.hajun.inventory.stock.controller;

import com.hajun.inventory.stock.domain.StockMovement;
import com.hajun.inventory.stock.dto.StockMovementResponse;
import com.hajun.inventory.stock.dto.StockRequest;
import com.hajun.inventory.stock.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/in")
    public void stockIn(@Valid @RequestBody StockRequest req) {
        stockService.stockIn(req.itemId(), req.warehouseId(), req.quantity());
    }

    @PostMapping("/out")
    public void stockOut(@Valid @RequestBody StockRequest req) {
        stockService.stockOut(req.itemId(), req.warehouseId(), req.quantity());
    }

    @PostMapping("/adjust")
    public void adjust(@Valid @RequestBody StockRequest req) {
        // StockRequest를 재사용한다면 quantity를 deltaQty로 해석 (음수 허용)
        stockService.adjust(req.itemId(), req.warehouseId(), req.quantity());
    }

    @GetMapping("/movements")
    public List<StockMovementResponse> movements(
            @RequestParam Long itemId,
            @RequestParam Long warehouseId
    ) {
        List<StockMovement> movements = stockService.movements(itemId, warehouseId);
        return movements.stream()
                .map(StockMovementResponse::from)
                .toList();
    }
}
