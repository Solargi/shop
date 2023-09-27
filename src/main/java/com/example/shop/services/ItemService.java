package com.example.shop.services;

import com.example.shop.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional //all methods in services are seen as transactions, if fails -> rolls back db
// @Transactional can be used to class lever for all methods or method lever for single methods
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
}
