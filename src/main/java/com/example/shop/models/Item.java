package com.example.shop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToMany(mappedBy = "item", orphanRemoval = true)
    private List<OrderItem> orderItems;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private BigDecimal availableQuantity;
}
