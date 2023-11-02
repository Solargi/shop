package com.example.shop.dtos.converters;

import com.example.shop.dtos.CartItemRequestDTO;
import com.example.shop.dtos.CartItemResponseDTO;
import com.example.shop.models.CartItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CartItemRequestDTOToCartItemConverter implements Converter<CartItemRequestDTO,CartItem> {
    @Override
    public CartItem convert(CartItemRequestDTO source) {
        CartItem cartItem = new CartItem();
        cartItem.setId(source.id());
        cartItem.setQuantity(source.quantity());
        return cartItem;
    }
}
