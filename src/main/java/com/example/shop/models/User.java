package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<Address> addresses;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String birthDate;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItem;
    @OneToMany(mappedBy = "user")
    private List<Order> orderList;
    @NotNull
    private String roles;
}
