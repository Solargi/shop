package com.example.shop.models;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.system.exceptions.GenericException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class CartItem {
    @EmbeddedId
    @NotNull
    private CartItemId id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;
    @NotNull
    private Integer quantity;

    @NotNull
    BigDecimal totalCost;

    public CartItem(User user, Item item, Integer quantity) {
        this.id = new CartItemId(user.getId(), item.getId());
        this.user = user;
        this.item = item;
        this. quantity =quantity;
        this.totalCost = computeTotalCost();

    }

    //tries to increase quantity by 1 if possible
    //if item out of stock throws Generic exception
    public void increaseQuantityBy1 (){
        this.setQuantity(this.getQuantity()+1);
        this.setTotalCost(this.computeTotalCost());
    }

    //tries to decrease quantity by 1 if possible
    //if CartItem quantity = 0 throws Generic exception
    public void decreaseQuantityBy1 (){
        if (this.getQuantity()>0){
            this.setQuantity(this.getQuantity()-1);
            this.setTotalCost(this.computeTotalCost());
        } else {
            throw new GenericException("cartItem quantity already 0 it can't be decreased");
        }

    }

    public void modifyQuantity (Integer quantity){
        if (quantity >= 0 ){
            this.setQuantity(this.getQuantity()+quantity);
        } else {
            if(this.getQuantity() + quantity < 0 ){
                throw   new GenericException("Can't decrease CartItem quantity by: "
                + quantity + "current quantity is :" + this.getQuantity());
            } else {
                this.setQuantity(this.getQuantity()+quantity);
            }
        }
        //then recompute total cost
        this.setTotalCost(this.computeTotalCost());
    }


    public BigDecimal computeTotalCost(){
        return  this.getItem().getPrice().multiply(BigDecimal.valueOf(getQuantity()));
    }
}
