package com.example.shop.models;

import com.example.shop.Embeddables.CartItemId;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class CartItem {
    @EmbeddedId
    @NotNull
    private CartItemId id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;
    @NotNull
    private Integer quantity;

    @NotNull
    BigDecimal totalCost;

    public CartItem(User user, Item item, Integer quantity) {
        this.id = new CartItemId(user.getId(), item.getId());
        this.user = user;
        this.item = item;
        this. quantity =quantity;
        this.totalCost = computeTotalCost();

    }

    public BigDecimal computeTotalCost(){
        return  this.getItem().getPrice().multiply(BigDecimal.valueOf(getQuantity()));
    }
}
