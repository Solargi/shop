package com.example.shop.controllers;

import com.example.shop.models.Item;
import com.example.shop.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController (ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    private ResponseEntity<Item> getItem(@PathVariable("itemId") int itemId){
        Item foundItem = this.itemService.findById(itemId);
        return ResponseEntity.ok(foundItem);
    }

}
