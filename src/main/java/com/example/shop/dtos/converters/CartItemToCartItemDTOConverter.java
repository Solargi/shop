package com.example.shop.dtos.converters;

import com.example.shop.dtos.CartItemDTO;
import com.example.shop.models.CartItem;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartItemToCartItemDTOConverter implements Converter<CartItem, CartItemDTO> {
    UserToUserDTOConverter userToUserDTOConverter;
    ItemToItemDTOConverter itemToItemDTOConverter;

    @Override
    public CartItemDTO convert(CartItem source) {
        return new CartItemDTO(source.getId(),
                this.userToUserDTOConverter.convert(source.getUser()),
                this.itemToItemDTOConverter.convert(source.getItem()),
                source.getQuantity(), source.getTotalCost());
    }
}
