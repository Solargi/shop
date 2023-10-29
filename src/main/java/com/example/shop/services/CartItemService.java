package com.example.shop.services;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.models.CartItem;
import com.example.shop.models.Item;
import com.example.shop.models.OrderItem;
import com.example.shop.models.User;
import com.example.shop.repositories.CartItemRepository;
import com.example.shop.repositories.ItemRepository;
import com.example.shop.repositories.UserRepository;
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
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public CartItem findById(CartItemId cartItemId){
        return this.cartItemRepository.findById(cartItemId).orElseThrow(() -> new ObjectNotFoundException("cartitem",cartItemId));
    }


    public List<CartItem> findAll(){
        return this.cartItemRepository.findAll();
    }

    //TODO TEST SERVICE:
    public List<CartItem> findAllByUserId(Integer userId){
        return this.cartItemRepository.findAllByUserId(userId);
    }

    // save works a bit different since we have 2 already existing entities, user and item
    // we first make sure that both exist, we recover them and set them in the Cartitem object
    // and recompute the total cost

    //TODO REWRITE SERVICE TESTS
    public CartItem save(CartItem cartitem){
        //if condition also enforced in dto validation
        if (cartitem.getQuantity() > 0) {
            User user = this.userRepository.findById(cartitem.getId().getUserId()).orElseThrow(() ->
                    new ObjectNotFoundException("user", cartitem.getId().getUserId()));
            Item item = this.itemRepository.findById(cartitem.getId().getItemId()).orElseThrow(() ->
                    new ObjectNotFoundException("item", cartitem.getId().getItemId()));
            //setting item, user and cost prevent null or wrong information
            cartitem.setItem(item);
            cartitem.setUser(user);
            cartitem.setTotalCost(cartitem.computeTotalCost());
            //assign/cartItem to lists in user and item. the instances fetched from db are
            //persistent entities so no need to save the entities to apply changes
            CartItem savedCartItem = this.cartItemRepository.save(cartitem);
            user.addCartItem(cartitem);
            item.addCartItem(cartitem);
            return savedCartItem;
        } else {
            throw new ObjectNotFoundException("CartItem quantity must be greater than 0 it was:", cartitem.getQuantity());
        }
    }

    public CartItem update(CartItemId cartItemId,  CartItem update){
        CartItem oldItem = this.cartItemRepository.findById(cartItemId).orElseThrow(()->new ObjectNotFoundException("cartitem",cartItemId));
        oldItem.setItem(update.getItem());
        oldItem.setQuantity(update.getQuantity());
        oldItem.setUser(update.getUser());
        oldItem.setId(update.getId());
        return this.cartItemRepository.save(oldItem);
    }

    //TODO REWRITE SERVICE TESTS
    public void delete(CartItemId cartItemId){
        CartItem cartitem = this.cartItemRepository.findById(cartItemId).orElseThrow(()-> new ObjectNotFoundException("cartitem",cartItemId));
        this.cartItemRepository.deleteById(cartItemId);
        //delete caritem from user and item /-> theoretically not necessary TODO TEST THIS ON INTEGRATION
        User user = this.userRepository.findById(cartitem.getId().getUserId()).orElseThrow(() ->
                new ObjectNotFoundException("user", cartitem.getId().getUserId()));
        Item item = this.itemRepository.findById(cartitem.getId().getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("item", cartitem.getId().getItemId()));
        user.removeCartItem(cartitem);
        item.removeCartItem(cartitem);
    }

    //TODO TEST SERVICE:
    public void deleteAllByUserId(Integer userId){
        List<CartItem> cartItems = this.cartItemRepository.findAllByUserId(userId);
        if (!cartItems.isEmpty()){
            for (CartItem cartItem : cartItems){
                Item item = this.itemRepository.findById(cartItem.getId().getItemId()).orElseThrow(() ->
                        new ObjectNotFoundException("item", cartItem.getId().getItemId()));
                item.removeCartItem(cartItem);
            }
            User user = this.userRepository.findById(userId).orElseThrow(() ->
                    new ObjectNotFoundException("user", userId));
            user.removeAllCartItems();
            this.cartItemRepository.deleteAllByUserId(userId);
        }
    }



}
