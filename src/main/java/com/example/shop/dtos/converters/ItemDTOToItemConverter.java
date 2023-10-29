package com.example.shop.dtos.converters;

import com.example.shop.dtos.ItemDTO;
import com.example.shop.models.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemDTOToItemConverter implements Converter<ItemDTO, Item> {
    @Override
    public Item convert(ItemDTO source) {
        Item item = new Item();
        item.setId(source.id());
        item.setName(source.name());
        item.setPrice(source.price());
        item.setDescription(source.description());
        item.setImageUrl(source.imageUrl());
        item.setAvailableQuantity(source.availableQuantity());
        return item;
    }
}
