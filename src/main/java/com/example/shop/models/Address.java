package com.example.shop.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn can be used to change the name of the user column
    @JoinColumn(name = "user_id")
    private User user;
    private String country;
    private String state;
    private String city;
    private String street;
    private Integer zipCode;

}
