package com.example.shop.services;

import com.example.shop.models.*;
import com.example.shop.repositories.*;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor

public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;


    public Order findById(Integer orderId){
        return this.orderRepository.findById(orderId).orElseThrow(() -> new ObjectNotFoundException("order",orderId));
    }

    public List<Order> findAll(){
        return this.orderRepository.findAll();
    }

    //to save the order we need the id of the user and the cart items of the user,
    // cart items are converted into orderitems and addet to the order that is then added to the user
    // cart items are then deleted from usrer
    public Order save(Order order){
        //fetch user if exists
        User user = this.userRepository.findById(order.getUser().getId()).orElseThrow(() ->
                new ObjectNotFoundException("user", order.getUser().getId()));
        //if CartItemList not empty:
        // save the order
        if (user.getCartItems().isEmpty()){
            // optional find cartItems list to convert to orderItems (assumes that cartItemlist of fetched user
            // is a valid list of valid items with valid total prices)
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            for (CartItem cartItem : user.getCartItems()){
                orderItems.add(new OrderItem(order, cartItem));
            }

            //set default order parameters to avoid false data
            //might want to remove this and put it in userservice
            //lock pure crud to admin? and do specific order crud inside user for users?
            order.setStatus("processing");
            order.setOrderItemList(orderItems);
            order.setTotalCost(order.computeTotalCost());
            order.setShippingCost(new BigDecimal(10));
            order.setUser(user);
            //saving order should generate key for order and order items
            Order savedOrder = orderRepository.save(order);
            //assign order to user after saving order
            user.addOrder(savedOrder);
            //user should be saved/persisted automatically since persistent instance is loaded
            // thus order should be cascaded updated as well

            //assign order item to item after saving and remove cart item -> technically not necessary since
            // the fetched order Item already has a valid reference to item in db TODO TEST THIS IN INTEGRATION
            for (OrderItem orderItem : savedOrder.getOrderItemList()){
                Item item = itemRepository.findById(orderItem.getItem().getId()).orElseThrow(() ->
                        new ObjectNotFoundException("item", orderItem.getItem().getId()));
                item.addOrderItem(orderItem);
            }

            //technically is enough to jsut remove cartItems-> TODO TEST THIS IN INTEGRATION TEST
            for (CartItem cartItem : user.getCartItems()){
                Item item = itemRepository.findById(cartItem.getItem().getId()).orElseThrow(() ->
                        new ObjectNotFoundException("item", cartItem.getItem().getId()));
                item.removeCartItem(cartItem);
                //delete caritem from db
                this.cartItemRepository.delete(cartItem);
            }
            // delete user cart items
            user.removeAllCartItems();

            //return saved order
            return savedOrder;

        }
        throw new RuntimeException("fuck");
    }

    public Order update(Integer orderId,  Order update){
        Order oldOrder = this.orderRepository.findById(orderId).orElseThrow(()->new ObjectNotFoundException("order",orderId));
        oldOrder.setUser(update.getUser());
        oldOrder.setOrderItemList(update.getOrderItemList());
        oldOrder.setShippingAddress(update.getShippingAddress());
        oldOrder.setShippingCost(update.getShippingCost());
        oldOrder.setStatus(update.getStatus());
        oldOrder.setTotalCost();
        return this.orderRepository.save(oldOrder);
    }

    public void delete(Integer orderId){
        Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ObjectNotFoundException("order",orderId));
        this.orderRepository.deleteById(orderId);
    }
}