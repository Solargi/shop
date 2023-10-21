package com.example.shop.dtos;

import com.example.shop.Embeddables.CartItemId;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CartItemDTO (CartItemId id,
                           @NotNull
                          UserDTO userDTO,
                           @NotNull
                          ItemDTO itemDTO,
                           @NotNull
                          Integer quantity) {
}
