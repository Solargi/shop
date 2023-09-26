package com.example.shop.models;

import com.example.shop.Embeddables.OrderItemId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor

public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;
    private Integer quantity;
    private BigDecimal totalCost;

    public OrderItem (Order order, Item item, Integer quantity, BigDecimal totalCost){
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }


}
