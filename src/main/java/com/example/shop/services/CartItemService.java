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
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
        Optional<CartItem> foundCartItemOpt = this.cartItemRepository.findById(cartitem.getId());
        // if cartItem already exist increase quantity and recompute cost
        if (foundCartItemOpt.isPresent()){
            CartItem foundCartItem = foundCartItemOpt.get();
            foundCartItem.modifyQuantity(cartitem.getQuantity());
            return this.cartItemRepository.save(foundCartItem);
        } else {
            //if cart item doesn't exist check that quantity is positive
            // if it doesn't exist fetch user and item
            // to make sure that CartItemId is valid
            User user = new User();
            Item item = new Item();
            this.verifyIdAndSetData(cartitem, user, item);
            // set all cartItem fields and recompute total cost
            CartItem savedCartItem = this.cartItemRepository.save(cartitem);
            user.addCartItem(cartitem);
            item.addCartItem(cartitem);
            return savedCartItem;
        }
    }

    public CartItem update(CartItemId cartItemId,  CartItem update){
        CartItem oldItem = this.cartItemRepository.findById(cartItemId).orElseThrow(()->new ObjectNotFoundException("cartitem",cartItemId));
        // make sure update cart item id is valid
        User user = new User();
        Item item = new Item();
        this.verifyIdAndSetData(update,user,item);
        // update old item and save
        oldItem.setItem(update.getItem());
        oldItem.setQuantity(update.getQuantity());
        oldItem.setUser(update.getUser());
        oldItem.setId(update.getId());
        oldItem.setTotalCost(oldItem.computeTotalCost());
        return this.cartItemRepository.save(oldItem);
    }

    //TODO REWRITE SERVICE TESTS
    public void delete(CartItemId cartItemId){
        CartItem cartitem = this.cartItemRepository.findById(cartItemId).orElseThrow(()-> new ObjectNotFoundException("cartitem",cartItemId));
        this.cartItemRepository.deleteById(cartItemId);;
    }

    //TODO TEST SERVICE:
    public void deleteAllByUserId(Integer userId){
            this.cartItemRepository.deleteAllByUserId(userId);
    }

    private void verifyIdAndSetData(CartItem cartItem, User user, Item item){
        user = this.userRepository.findById(cartItem.getId().getUserId()).orElseThrow(() ->
                new ObjectNotFoundException("user", cartItem.getId().getUserId()));
        item = this.itemRepository.findById(cartItem.getId().getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("item", cartItem.getId().getItemId()));
        //set right fields in cartItem
        cartItem.setItem(item);
        cartItem.setUser(user);
        cartItem.setTotalCost(cartItem.computeTotalCost());

    }


}
