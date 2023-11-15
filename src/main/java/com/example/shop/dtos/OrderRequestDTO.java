package com.example.shop.dtos;

import com.example.shop.dtos.AddressDTO;
import com.example.shop.dtos.UserDTO;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequestDTO(@NotNull
                              Integer id,
                              @NotNull
                              String status,
                              @NotNull
                              AddressDTO shippingAddress,
                              @NotNull
                              BigDecimal shippingCost,
                              @NotNull
                              BigDecimal totalCost,
                              @NotNull
                              Boolean paid
) {
}
