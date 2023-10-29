package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data //a shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;
    @NotNull
    @NotEmpty
    private String email;
    @NotEmpty
    @NotNull
    private String password;
    @NotNull
    private String birthDate;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
    @OneToMany(mappedBy = "user")
    private List<Order> orderList;
    @NotNull
    @NotEmpty
    private String roles;

    public void addCartItem(CartItem cartItem){
        if (!this.cartItems.contains(cartItem)) {
            this.cartItems.add(cartItem);
        }
    }
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
    }

    public void removeAllCartItems(){
        setCartItems(new ArrayList<CartItem>());
    }

    public void addOrder(Order order){
        if (!this.orderList.contains(order)) {
            this.orderList.add(order);
        }
    }
    public void removeOrder(Order order){this.orderList.remove(order);}
}
