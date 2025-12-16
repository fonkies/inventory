package com.hajun.inventory.item.controller;

import com.hajun.inventory.item.dto.ItemCreateRequest;
import com.hajun.inventory.item.dto.ItemResponse;
import com.hajun.inventory.item.dto.ItemUpdateRequest;
import com.hajun.inventory.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> create(@RequestBody ItemCreateRequest request) {
        ItemResponse created = itemService.create(request);
        return ResponseEntity
                .created(URI.create("/api/items/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ItemResponse get(@PathVariable Long id) {
        return itemService.get(id);
    }

    @GetMapping
    public List<ItemResponse> list() {
        return itemService.list();
    }

    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable Long id, @RequestBody ItemUpdateRequest request) {
        return itemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
