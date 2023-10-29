package com.example.shop.repositories;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
