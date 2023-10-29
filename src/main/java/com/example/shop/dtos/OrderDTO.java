package com.example.shop.dtos;

import com.example.shop.models.Address;
import com.example.shop.models.OrderItem;
import com.example.shop.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderDTO (Integer id,
                        @NotNull
                        User user,
                        @NotNull
                        String status,
                        @NotNull
                        Address shippingAddress,
                        @NotNull
                        BigDecimal shippingCost,
                        @NotNull
                        BigDecimal totalCost,
                        @NotNull
                        Boolean paid
                        ){}