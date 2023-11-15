package com.example.shop.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderResponseDTO(Integer id,
                               @NotNull
                        UserDTO user,
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
                        ){}