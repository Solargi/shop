package com.example.shop.services;

import com.example.shop.models.*;
import com.example.shop.repositories.*;
import com.example.shop.system.exceptions.GenericException;
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

    public List<Order> findAllByUserId(Integer userId){
        return this.orderRepository.findAllByUserId(userId);
    }

    public List<Order> findAll(){
        return this.orderRepository.findAll();
    }

    //to save the order we need the id of the user and the cart items of the user
    // the id or name of the user will be used to fetch the user and the relative cart item
    // the order will be automatically be created using the cart items of the user
    // cart items are converted into orderitems and added to the order that is then added to the user
    // cart items are then deleted from usrer
    //cart items are also deleted from items while orderitems are added.
    public Order save(Integer userId){
        Order order = new Order();
        //fetch user if exists
        User user = this.userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("user", userId));
        //if CartItemList not empty:
        // create and save the order
        if (!user.getCartItems().isEmpty()){
            if(user.getAddresses().isEmpty()){
                throw new GenericException("User doesn't have a shipping address");
            }
            //set default order parameters to avoid false data
            //might want to remove this and put it in userservice
            //lock pure crud to admin? and do specific order crud inside user for users?
            order.setStatus("processing");
            order.setPaid(true);
            order.setShippingCost(new BigDecimal(10));
            order.setShippingAddress(user.getAddresses().get(0));
            order.setUser(user);
            List<CartItem> cartItemsToRemove = new ArrayList<>();
            // find cartItems list to convert to orderItems (assumes that cartItemlist of fetched user
            // is a valid list of valid items with valid total prices)
            for (CartItem cartItem : user.getCartItems()){
                //we have to decrease item available quantity after creating orderItem
                Item item = itemRepository.findById(cartItem.getItem().getId()).orElseThrow(() ->
                        new ObjectNotFoundException("item", cartItem.getItem().getId()));
                int cartItemQuantity = cartItem.getQuantity();
                if(cartItemQuantity > 0 && cartItemQuantity <= item.getAvailableQuantity().intValue()){
                    order.addOrderItem(new OrderItem(order, cartItem));
                    item.modifyQuantity(-cartItemQuantity);
                    itemRepository.save(item);
                    cartItemsToRemove.add(cartItem);
                } else {
                    cartItem.setQuantity(item.getAvailableQuantity().intValue());
                    if (cartItem.getQuantity() <= 0 ){
                        cartItemsToRemove.add(cartItem);
                    }
                }
            }
            user.removeCartItems(cartItemsToRemove);
            order.setTotalCost(order.computeTotalCost());



//            saving order generates key for order and order items
            if (order.getOrderItemList().isEmpty()){
                throw new GenericException("no orderItems found in the order. Check available quantities before placing orders");
            }

            Order savedOrder = orderRepository.save(order);


            //assign order to user after saving order
//            user.addOrder(savedOrder);
//            //user should be saved/persisted automatically since persistent instance is loaded
//            // thus order should be cascaded updated as well
//
//            //assign order item to item after saving and remove cart item -> not necessary since
//            the owning side (orderItem already has a valid reference to an item id thus when the item
//            is fetched from db it will have the reference to orderItem)

//            for (OrderItem orderItem : savedOrder.getOrderItemList()){
//                Item item = itemRepository.findById(orderItem.getItem().getId()).orElseThrow(() ->
//                        new ObjectNotFoundException("item", orderItem.getItem().getId()));
//                item.addOrderItem(orderItem);
//            }
//
//            //it's enough to jsut remove cartItems from user-> since user is a persistent instance
//            and cartItem key is composed of user and Item, removing the cartItems from user will also delete the
//            related cartItem instances from db thus newly fetched items will lose the corresponding cartItem as well
//            for (CartItem cartItem : user.getCartItems()){
//                Item item = itemRepository.findById(cartItem.getItem().getId()).orElseThrow(() ->
//                        new ObjectNotFoundException("item", cartItem.getItem().getId()));
//                item.removeCartItem(cartItem);
//                //delete caritem from db
//                this.cartItemRepository.delete(cartItem);
//            }
//            // delete user cart items
//            user.removeAllCartItems();
//
//            //return saved order
            return savedOrder;

        }
        throw new GenericException("User's cart has no items in it");
    }

    public Order update(Integer orderId,  Order update){
        Order oldOrder = this.orderRepository.findById(orderId).orElseThrow(()->new ObjectNotFoundException("order",orderId));
        //user can't be updated
//        oldOrder.setUser(update.getUser());
        //orderitem list can't be updated here directly,
//        better to update orderItems singularly
//        oldOrder.setOrderItemList(update.getOrderItemList());
        oldOrder.setShippingAddress(update.getShippingAddress());
        oldOrder.setShippingCost(update.getShippingCost());
        oldOrder.setStatus(update.getStatus());
        oldOrder.updateTotalCost();
        return this.orderRepository.save(oldOrder);
    }

    public void delete(Integer orderId){
        Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ObjectNotFoundException("order",orderId));
        this.orderRepository.deleteById(orderId);
    }
}
