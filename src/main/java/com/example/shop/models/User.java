package com.example.shop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data //a shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String surname;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Address> addresses;
    private String email;
    private String password;
    private String birthDate;
    @OneToOne
    private ShoppingCart shoppingCart;
    @OneToMany
    private List<Order> orderList;
    private String roles;
}
