package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private User user;
    private String status;
    private Integer total_price;
    @OneToMany
    private List<Item> itemList;
    @OneToOne(fetch = FetchType.LAZY)
    private Address shippingAddress;


}
