package com.example.shop.dtos.converters;

import com.example.shop.dtos.ItemDTO;
import com.example.shop.models.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemToItemDTOConverter implements Converter<Item, ItemDTO> {
    @Override
    public ItemDTO convert(Item source) {
        return new ItemDTO(source.getId(),
                source.getName(),
                source.getDescription(),
                source.getPrice(),
                source.getImageUrl(),
                source.getAvailableQuantity());
    }
}
