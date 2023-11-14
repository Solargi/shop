package com.example.shop.dtos;

import com.example.shop.Embeddables.CartItemId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CartItemResponseDTO(@NotNull
                           CartItemId id,
                                  // user and item dto must be manually assigned
                                  // is services to make sure data is consistent with db
                                  UserDTO userDTO,
                                  ItemDTO itemDTO,
                                  @NotNull
                                  @Positive
                                  Integer quantity,
                                  //setted in services before saving
                                  //or updating to avoid wrong / null values
                                  BigDecimal totalCost) {
}
