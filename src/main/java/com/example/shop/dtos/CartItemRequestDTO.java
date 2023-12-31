package com.example.shop.dtos;

import com.example.shop.Embeddables.CartItemId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDTO(
        @NotNull
        @Positive
        Integer quantity
) {
}
