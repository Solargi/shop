package com.example.shop.repositories;

import com.example.shop.models.CartItem;
import com.example.shop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository <Order, Integer> {
    public List<Order> findAllByUserId(Integer userId);
}
