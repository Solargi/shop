package com.example.shop.models;

import com.example.shop.Embeddables.OrderItemId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor

public class OrderItem {
    @EmbeddedId
    @NotNull
    private OrderItemId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @NotNull
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;
    @NotNull
    private Integer quantity;
    @NotNull
    private BigDecimal totalCost;

    public OrderItem (Order order, Item item, Integer quantity){
        this.id = new OrderItemId(order.getId(), item.getId());
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.totalCost = this.computeTotalCost();
    }
    public OrderItem (Order order, CartItem cartItem){
        this.id = new OrderItemId(order.getId(),cartItem.getItem().getId());
        this.order = order;
        this.item = cartItem.getItem();
        this.totalCost = cartItem.getTotalCost();
        this.quantity = cartItem.getQuantity();
    }

    public void updateTotalCost(){
        this.setTotalCost(this.computeTotalCost());
        this.order.updateTotalCost();
    }

    public void removeFromOrder (){
        this.order.removeOrderItem(this);
    }

    public BigDecimal computeTotalCost () {
        return this.item.getPrice().multiply(new BigDecimal(this.getQuantity()));
    }


}
