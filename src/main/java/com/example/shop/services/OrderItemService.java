package com.example.shop.services;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.models.Item;
import com.example.shop.models.Order;
import com.example.shop.models.OrderItem;
import com.example.shop.repositories.ItemRepository;
import com.example.shop.repositories.OrderItemRepository;
import com.example.shop.repositories.OrderRepository;
import com.example.shop.system.exceptions.GenericException;
import com.example.shop.system.exceptions.ObjectAlreadyExistException;
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
        Integer quantity = orderItem.getQuantity();
        if (quantity > 0 && quantity <= item.getAvailableQuantity().intValue()){
            updateItem(item.getId(),-quantity);
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setTotalCost(orderItem.computeTotalCost());
            OrderItem savedOrderItem = this.orderItemRepository.save(orderItem);
            //add savedOrderItem to current persistent instance of Order
            order.addOrderItem(savedOrderItem);
            //update order total
            order.updateTotalCost();
            return savedOrderItem;
        } else {
            throw new GenericException("order quantity is higher than item's available quantity");
        }
    }

    //only allowed update is quantity
    public OrderItem update(OrderItem update){
        OrderItemId orderItemId = update.getId();
        OrderItem oldOrderItem = this.orderItemRepository.findById(orderItemId)
                .orElseThrow(()->new ObjectNotFoundException("orderItem",orderItemId));
//        oldOrderItem.setOrder(update.getOrder());
//        oldOrderItem.setItem(update.getItem());
//        oldOrderItem.setId(update.getId());
        Integer quantityDifference = oldOrderItem.getQuantity() - update.getQuantity();
        // if same quantity or more than original subtract difference form available quantity
        //(update.getQuanitity can't be negative controlled in DTO)
        if (quantityDifference <= 0){
            updateItem(oldOrderItem.getId().getItemId(), quantityDifference);
        }
        // if quantity less than original add back difference to available quantity
        if (quantityDifference > 0 && quantityDifference < oldOrderItem.getQuantity()){
            updateItem(oldOrderItem.getId().getItemId(), quantityDifference);
        }

        oldOrderItem.setQuantity(update.getQuantity());
        oldOrderItem.setTotalCost(oldOrderItem.computeTotalCost());
        OrderItem savedOrderItem = this.orderItemRepository.save(oldOrderItem);
        //update order total price
        this.updateOrderCost(oldOrderItem.getId().getOrderId());
        return savedOrderItem;
    }

    public void delete(OrderItemId orderItemId){
        OrderItem orderItem = this.orderItemRepository.findById(orderItemId).orElseThrow(()-> new ObjectNotFoundException("orderItem",orderItemId));
        Integer orderId = orderItem.getId().getOrderId();
        Integer itemId = orderItem.getId().getItemId();
        Integer quantity = orderItem.getQuantity();
        orderItem.removeFromOrder();
        this.orderItemRepository.deleteById(orderItemId);

        //recompute order total cost would be better to set total cost as a computed property in the db
        this.updateOrderCost(orderId);

        //restore item availabe quantity
        this.updateItem(itemId, quantity);

    }

//    private void updateOrderTotalCost(OrderItem orderItem){
//        Order order = this.orderRepository.findById(orderItem.getId().getOrderId())
//                .orElseThrow(() -> new ObjectNotFoundException("order", orderItem.getId().getOrderId()));
//        order.updateTotalCost();
//    }

    private void updateItem (Integer itemId, Integer quantity){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()->new ObjectNotFoundException("item", itemId));
        item.modifyQuantity(quantity);
        this.itemRepository.save(item);

    }
    private void updateOrderCost(Integer orderId){
        //recompute order total cost would be better to set total cost as a computed property in the db
        Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ObjectNotFoundException("order",orderId));
        order.setTotalCost(order.computeTotalCost());
        this.orderRepository.save(order);
    }



}
