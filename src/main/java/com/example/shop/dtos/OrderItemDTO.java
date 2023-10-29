package com.example.shop.dtos;

import com.example.shop.Embeddables.OrderItemId;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemDTO (@NotNull
                            OrderItemId id,
                            OrderDTO orderDTO,
                            ItemDTO itemDTO,
                            Integer quantity,
                            BigDecimal totalCost){
}
