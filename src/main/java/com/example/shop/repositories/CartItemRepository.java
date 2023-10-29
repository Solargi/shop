package com.example.shop.repositories;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    public void deleteAllByUserId (Integer userId);
    public List<CartItem> findAllByUserId(Integer userId);

}
