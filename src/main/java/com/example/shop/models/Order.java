package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;
    @NotNull
    private String status;
    @NotNull
    private Integer total_price;
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    @NotNull
    private List<OrderItem> orderItemList;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Address shippingAddress;


}
