package com.example.shop.dtos.converters;

import com.example.shop.dtos.CartItemResponseDTO;
import com.example.shop.models.CartItem;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartItemToCartItemResponseDTOConverter implements Converter<CartItem, CartItemResponseDTO> {
    UserToUserDTOConverter userToUserDTOConverter;
    ItemToItemDTOConverter itemToItemDTOConverter;

    @Override
    public CartItemResponseDTO convert(CartItem source) {
        return new CartItemResponseDTO(source.getId(),
                this.userToUserDTOConverter.convert(source.getUser()),
                this.itemToItemDTOConverter.convert(source.getItem()),
                source.getQuantity(), source.getTotalCost());
    }
}
