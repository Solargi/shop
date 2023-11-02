package com.example.shop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
//    @NotNull
    private List<OrderItem> orderItemList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Address shippingAddress;
    @NotNull
    private BigDecimal shippingCost;
    @NotNull
    private boolean paid;

    public BigDecimal computeTotalCost () {
        BigDecimal total = new BigDecimal(0);
        if (!orderItemList.isEmpty()){
            for (OrderItem orderItem : orderItemList){
                total = total.add(orderItem.getTotalCost());
//                System.out.println("total : " + total);
//                System.out.println("orderItems : " + this.getOrderItemList().size());
            }
            total = total.add(shippingCost);
        }
        return total;
    }
    public void addOrderItem(OrderItem orderItem){
        if (!this.orderItemList.contains(orderItem)) {
            this.orderItemList.add(orderItem);
            orderItem.setOrder(this);
            this.updateTotalCost();
        }
    }
    public void removeOrderItem(OrderItem orderItem){
        this.orderItemList.remove(orderItem);
        this.updateTotalCost();
    }
    public void removeAllOrderItems(){
        this.orderItemList.clear();
        this.updateTotalCost();
    }



    public void updateTotalCost() {
        this.setTotalCost(this.computeTotalCost());
    }
    public boolean getPaid(){
        return this.paid;
    }
}
