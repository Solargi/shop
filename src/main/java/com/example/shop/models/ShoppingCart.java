package com.example.shop.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ShoppingCart {
    @Id
    private Integer id;
}
