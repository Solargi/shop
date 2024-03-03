package com.example.shop.dtos;

import com.example.shop.Embeddables.CartItemId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CartItemResponseDTO(@NotNull
                           CartItemId id,
                                  UserDTO userDTO,
                                  ItemDTO itemDTO,
                                  @NotNull
                                  @Positive
                                  Integer quantity,
                                  BigDecimal totalCost) {
}
