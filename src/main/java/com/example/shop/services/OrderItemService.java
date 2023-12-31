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
       return this.orderItemRepository.findById(orderItemId).orElseThrow(()->new ObjectNotFoundException("orderItem", orderItemId));
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
        OrderItem savedOrderItem = this.orderItemRepository.save(orderItem);
        //add savedOrderItem to current persitent instance of Order
        order.addOrderItem(savedOrderItem);
        //update order total
        order.updateTotalCost();
        return savedOrderItem;
    }

    //only allowed update is quantity
    public OrderItem update(OrderItem update){
        OrderItemId orderItemId = update.getId();
        OrderItem oldOrderItem = this.orderItemRepository.findById(orderItemId)
                .orElseThrow(()->new ObjectNotFoundException("orderItem",orderItemId));
//        oldOrderItem.setOrder(update.getOrder());
        oldOrderItem.setQuantity(update.getQuantity());
//        oldOrderItem.setItem(update.getItem());
//        oldOrderItem.setId(update.getId());
        oldOrderItem.setTotalCost(oldOrderItem.computeTotalCost());
        OrderItem savedOrderItem = this.orderItemRepository.save(oldOrderItem);
        //update order total price
        savedOrderItem.updateTotalCost();
        return savedOrderItem;
    }

    public void delete(OrderItemId orderItemId){
        OrderItem orderItem = this.orderItemRepository.findById(orderItemId).orElseThrow(()-> new ObjectNotFoundException("orderItem",orderItemId));
        orderItem.removeFromOrder();
        this.orderItemRepository.deleteById(orderItemId);
    }

//    private void updateOrderTotalCost(OrderItem orderItem){
//        Order order = this.orderRepository.findById(orderItem.getId().getOrderId())
//                .orElseThrow(() -> new ObjectNotFoundException("order", orderItem.getId().getOrderId()));
//        order.updateTotalCost();
//    }



}
