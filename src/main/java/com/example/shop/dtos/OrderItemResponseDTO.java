package com.example.shop.dtos;

import com.example.shop.Embeddables.OrderItemId;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemResponseDTO(@NotNull
                            OrderItemId id,
                                   OrderResponseDTO orderResponseDTO,
                                   ItemDTO itemDTO,
                                   Integer quantity,
                                   BigDecimal totalCost){
}
