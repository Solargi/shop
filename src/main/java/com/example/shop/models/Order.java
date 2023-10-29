package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
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
    private BigDecimal totalCost;
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    @NotNull
    private List<OrderItem> orderItemList;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Address shippingAddress;
    @NotNull
    private BigDecimal shippingCost;
    @NotNull
    private boolean paid;

    public BigDecimal computeTotalCost () {
        BigDecimal total = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList){
            total = total.add(orderItem.getTotalCost());
        }
        total = total.add(shippingCost);
        return total;
    }

    public void setTotalCost () {
        this.totalCost = this.computeTotalCost();
    }
    public boolean getPaid(){
        return this.paid;
    }
}
