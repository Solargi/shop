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
    @NotEmpty
    @Column(unique = true)
    private String username;
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String birthDate;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Order> orderList = new ArrayList<>();
    @NotEmpty
    private String roles;

    public void addCartItem(CartItem cartItem){
        if (!this.cartItems.contains(cartItem)) {
            this.cartItems.add(cartItem);
            cartItem.setUser(this);
        }
    }

    public void addAddress(Address address){
        if (!this.addresses.contains(address)) {
            this.addresses.add(address);
            address.setUser(this);
        }
    }
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
    }

    public void removeAllCartItems(){
        this.cartItems.clear();
    }

    public void addOrder(Order order){
        if (!this.orderList.contains(order)) {
            this.orderList.add(order);
        }
    }
    public void removeOrder(Order order){this.orderList.remove(order);}
}
