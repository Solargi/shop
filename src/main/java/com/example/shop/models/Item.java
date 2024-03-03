package com.example.shop.models;

import com.example.shop.system.exceptions.GenericException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Integer id;
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private BigDecimal price;
    private String imageUrl;
    @NotNull
    private BigDecimal availableQuantity;

    public void addCartItem(CartItem cartItem){
        if (!this.cartItems.contains(cartItem)) {
            this.cartItems.add(cartItem);
        }
    }
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
    }
    public void removeAllCartItems(){
        this.cartItems.clear();
    }

    public void addOrderItem(OrderItem orderItem){
        if (!this.orderItems.contains(orderItem)) {
            this.orderItems.add(orderItem);
        }
    }

    public void increaseAvailableQuantityBy1 (){
        if (this.getAvailableQuantity().compareTo(new BigDecimal(1))>=1){
            this.setAvailableQuantity(this.getAvailableQuantity().add(new BigDecimal(1)));
        } else {
            throw new GenericException("Item " + this.getName() + "Out of stock, can't decrease available quantity" +
                    "since its value is 0");
        }

    }
    public void decreaseAvailableQuantityBy1 (){
        if (this.getAvailableQuantity().compareTo(new BigDecimal(1))<1){
            this.setAvailableQuantity(this.getAvailableQuantity().subtract(new BigDecimal(1)));
        } else {
            throw new GenericException("Item " + this.getName() + "Out of stock, can't decrease available quantity" +
                    "since its value is 0");
        }
    }

    public void modifyQuantity(Integer quantity){
        // if quantity is positive add to available quantity
        if (quantity>0){
            this.setAvailableQuantity(this.availableQuantity.add(new BigDecimal(quantity)));
        } else {
            //if quantity is negative, check if there is enough available quantity
            // if so subtract the quantity from available
            if (this.getAvailableQuantity()
                    .add(new BigDecimal(quantity))
                    .compareTo(new BigDecimal(0)) >=0){
                this.setAvailableQuantity(this.availableQuantity.add(new BigDecimal(quantity)));
            } else { //else throw generic exception
                throw new GenericException("Item " + this.getName()
                        + " Out of stock, can't decrease available quantity of " +
                        quantity +
                        " current available quantity is : " + this.availableQuantity);
            }
        }
    }
    public void removeOrderItem(OrderItem orderItem){this.orderItems.remove(orderItem);}


}
