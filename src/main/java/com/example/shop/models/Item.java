package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public void addCartItem(CartItem cartItem){
        if (!this.cartItems.contains(cartItem)) {
            this.cartItems.add(cartItem);
        }
    }
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
    }
    public void removeAllCartItems(){
        this.cartItems.clear();
    }

    public void addOrderItem(OrderItem orderItem){
        if (!this.orderItems.contains(orderItem)) {
            this.orderItems.add(orderItem);
        }
    }
    public void removeOrderItem(OrderItem orderItem){this.orderItems.remove(orderItem);}


}
