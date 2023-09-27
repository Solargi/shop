package com.example.shop.controllers;

import com.example.shop.services.ItemService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    private final ItemService itemService;

    public ItemController (ItemService itemService){
        this.itemService = itemService;
    }
}
