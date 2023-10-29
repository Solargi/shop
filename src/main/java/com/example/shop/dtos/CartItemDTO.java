package com.example.shop.dtos;

import com.example.shop.Embeddables.CartItemId;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CartItemDTO (
                           CartItemId id,
                           //setted in services before saving
                           // and putting so can be null
                          UserDTO userDTO,
                           //setted in services before saving
                           // and putting so can be null
                          ItemDTO itemDTO,
                           @NotNull @Positive
                          Integer quantity,
                           //setted in services before saving
                           // and putting so can be null
                           BigDecimal totalCost) {
}
