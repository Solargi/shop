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
    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;
    private String email;
    private String password;
    private String birthDate;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItem;
    @OneToMany(mappedBy = "user")
    private List<Order> orderList;
    private String roles;
}
