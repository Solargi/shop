package com.example.shop.dtos.converters;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.dtos.CartItemRequestDTO;
import com.example.shop.models.CartItem;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartItemRequestDTOToCartItemConverter{
    public CartItem convert(CartItemRequestDTO source, Integer userId, Integer itemId) {
        //add id to object builded and added to object from url parameter for security reasons
        CartItemId cartItemId = new CartItemId(userId,itemId);
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(source.quantity());
        //add id to cartitem
        cartItem.setId(cartItemId);

        return cartItem;
    }
}
