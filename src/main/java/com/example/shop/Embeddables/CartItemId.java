package com.example.shop.Embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartItemId implements Serializable {
        @Column(name="user_id")
        private Integer userId;
        @Column(name = "item_id")
        private Integer itemId;
}
