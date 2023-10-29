package com.example.shop.services;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.models.Item;
import com.example.shop.models.Order;
import com.example.shop.models.OrderItem;
import com.example.shop.repositories.ItemRepository;
import com.example.shop.repositories.OrderItemRepository;
import com.example.shop.repositories.OrderRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class OrderItemService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> findAll(){
        return this.orderItemRepository.findAll();
    }
    public OrderItem findById (OrderItemId orderItemId){
       return this.orderItemRepository.findById(orderItemId).orElseThrow(()->new ObjectNotFoundException("OrderItem", orderItemId));
    }

    public OrderItem save(OrderItem orderItem){
        //just make sure data exist and it's correctly inserted into db
        Order order = this.orderRepository.findById(orderItem.getId().getOrderId())
                .orElseThrow(()->new ObjectNotFoundException("order", orderItem.getId().getOrderId()));
        Item item = this.itemRepository.findById(orderItem.getId().getItemId())
                .orElseThrow(()->new ObjectNotFoundException("item", orderItem.getId().getItemId()));
        orderItem.setItem(item);
        orderItem.setOrder(order);
        orderItem.setTotalCost(orderItem.computeTotalCost());
        return this.orderItemRepository.save(orderItem);
    }

    public OrderItem update(OrderItemId orderItemId,  OrderItem update){
        OrderItem oldOrderItem = this.orderItemRepository.findById(orderItemId).orElseThrow(()->new ObjectNotFoundException("orderItem",orderItemId));
        //TODO might have to change this to avoid insertion of order fro non existing users, items
        oldOrderItem.setOrder(update.getOrder());
        oldOrderItem.setQuantity(update.getQuantity());
        oldOrderItem.setItem(update.getItem());
        oldOrderItem.setId(update.getId());
        oldOrderItem.setTotalCost(oldOrderItem.computeTotalCost());
        return this.orderItemRepository.save(oldOrderItem);
    }

    public void delete(OrderItemId orderItemId){
        OrderItem orderItem = this.orderItemRepository.findById(orderItemId).orElseThrow(()-> new ObjectNotFoundException("orderItem",orderItemId));
        this.orderItemRepository.deleteById(orderItemId);
    }



}
