package com.example.shop.models;

import com.example.shop.Embeddables.CartItemId;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CartItem {
    @EmbeddedId
    private CartItemId id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer quantity;

    public CartItem(User user, Item item, Integer quantity) {
        this.id = new CartItemId(user.getId(), item.getId());
        this.user = user;
        this.item = item;
        this. quantity =quantity;

    }
}
