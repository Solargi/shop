package com.example.shop.dtos;

import com.example.shop.Embeddables.OrderItemId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequestDTO(
        @NotNull
        @Positive
        Integer quantity

) {
}
