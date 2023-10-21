package com.example.shop.repositories;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {

}
