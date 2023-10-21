package com.example.shop.services;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.models.CartItem;
import com.example.shop.repositories.CartItemRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItem findById(CartItemId cartItemId){
        return this.cartItemRepository.findById(cartItemId).orElseThrow(() -> new ObjectNotFoundException("cartitem",cartItemId));
    }


    public List<CartItem> findAll(){
        return this.cartItemRepository.findAll();
    }

    public CartItem save(CartItem cartitem){
        return this.cartItemRepository.save(cartitem);
    }

    public CartItem update(CartItemId cartItemId,  CartItem update){
        CartItem oldItem = this.cartItemRepository.findById(cartItemId).orElseThrow(()->new ObjectNotFoundException("cartitem",cartItemId));
        oldItem.setItem(update.getItem());
        oldItem.setQuantity(update.getQuantity());
        oldItem.setUser(update.getUser());
        oldItem.setId(update.getId());
        return this.cartItemRepository.save(oldItem);
    }

    public void delete(CartItemId cartItemId){
        CartItem cartitem = this.cartItemRepository.findById(cartItemId).orElseThrow(()-> new ObjectNotFoundException("cartitem",cartItemId));
        this.cartItemRepository.deleteById(cartItemId);
    }


}
