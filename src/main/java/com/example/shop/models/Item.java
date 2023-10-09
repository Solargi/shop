package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Integer id;
    @OneToMany(mappedBy = "item", orphanRemoval = true)
    private List<OrderItem> orderItems;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private BigDecimal price;
    private String imageUrl;
    @NotNull
    private BigDecimal availableQuantity;
}
