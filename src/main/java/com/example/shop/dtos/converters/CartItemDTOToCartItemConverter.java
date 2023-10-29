package com.example.shop.dtos.converters;

import com.example.shop.dtos.CartItemDTO;
import com.example.shop.models.CartItem;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartItemDTOToCartItemConverter implements Converter<CartItemDTO, CartItem> {
    UserDTOToUserConverter userDTOToUserConverter;
    ItemDTOToItemConverter itemDTOToItemConverter;


    @Override
    public CartItem convert(CartItemDTO source) {
        CartItem cartItem = new CartItem();
        cartItem.setId(source.id());
        cartItem.setUser(this.userDTOToUserConverter.convert(source.userDTO()));
        cartItem.setItem(this.itemDTOToItemConverter.convert(source.itemDTO()));
        cartItem.setQuantity(source.quantity());
        cartItem.setTotalCost(source.totalCost());
        return cartItem;
    }
}
