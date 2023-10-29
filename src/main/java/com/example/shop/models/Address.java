package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn can be used to change the name of the user column
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    @NotNull
    private String country;
    @NotNull
    private String state;
    @NotNull
    private String city;
    @NotNull
    private String street;
    @NotNull
    private Integer zipCode;

}
