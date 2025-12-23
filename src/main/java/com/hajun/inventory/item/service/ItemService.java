package com.hajun.inventory.item.service;

import com.hajun.inventory.global.exception.ConflictException;
import com.hajun.inventory.common.exception.NotFoundException;
import com.hajun.inventory.item.domain.Item;
import com.hajun.inventory.item.dto.ItemCreateRequest;
import com.hajun.inventory.item.dto.ItemResponse;
import com.hajun.inventory.item.dto.ItemUpdateRequest;
import com.hajun.inventory.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public ItemResponse create(ItemCreateRequest request) {
        if (itemRepository.existsByCode(request.code())) {
            throw new ConflictException("ITEM_CODE_DUPLICATE", "Item code already exists: " + request.code());
        }

        Item item = Item.create(request.code(), request.name());

        Item saved = itemRepository.save(item);
        return ItemResponse.from(saved);
    }

    public ItemResponse get(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found. id=" + id));
        return ItemResponse.from(item);
    }

    public List<ItemResponse> list() {
        return itemRepository.findAll().stream()
                .map(ItemResponse::from)
                .toList();
    }

    @Transactional
    public ItemResponse update(Long id, ItemUpdateRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found. id=" + id));

        // code 변경 시에만 중복 체크
        if (!item.getCode().equals(request.code()) && itemRepository.existsByCode(request.code())) {
            throw new ConflictException("ITEM_CODE_DUPLICATE", "Item code already exists: " + request.code());
        }

        item.update(request.code(), request.name(), item.isActive());
        return ItemResponse.from(item);
    }

    @Transactional
    public void delete(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ITEM_NOT_FOUND", "Item not found. id=" + id));
        itemRepository.delete(item);
    }
}
