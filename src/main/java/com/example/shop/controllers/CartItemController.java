package com.example.shop.controllers;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.dtos.CartItemDTO;
import com.example.shop.dtos.converters.CartItemDTOToCartItemConverter;
import com.example.shop.dtos.converters.CartItemToCartItemDTOConverter;
import com.example.shop.models.CartItem;
import com.example.shop.models.CartItem;
import com.example.shop.services.CartItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/cartItems")
@AllArgsConstructor
public class CartItemController {
    CartItemService cartItemService;
    CartItemToCartItemDTOConverter cartItemToCartItemDTOConverter;
    CartItemDTOToCartItemConverter cartItemDTOToCartItemConverter;
    @GetMapping("")
    public ResponseEntity<List<CartItemDTO>> getCartItems(){
        List<CartItem> foundCartItems = this.cartItemService.findAll();
        //convert to dtos
        List<CartItemDTO> foundItemsDTO = foundCartItems.stream()
                .map(this.cartItemToCartItemDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundItemsDTO);

    }
    @GetMapping("/{userId}/{itemId}")
    public ResponseEntity<CartItemDTO> findCartItemById(@PathVariable("userId") Integer userId,
                                                    @PathVariable("itemId") Integer itemId){
        CartItemId cartItemId = new CartItemId(userId, itemId);
        CartItem foundCartItem = this.cartItemService.findById(cartItemId);
        return ResponseEntity.ok(this.cartItemToCartItemDTOConverter.convert(foundCartItem));
    }

    //TODO: ADD A TEST ENDPOINT TO FETCH ALL CARTITEMS BELONGING TO A USER USING USERID

    @PostMapping("")
    //valid checks for validity of fields defined in ItemDto class with annotation
    // request body takes
    public ResponseEntity<CartItemDTO> addCartItem(@Valid @RequestBody CartItemDTO cartItemDTO){
        //convert dto to object
        CartItem cartItem = this.cartItemDTOToCartItemConverter.convert(cartItemDTO);

        //save cartItem
        CartItem savedItem = this.cartItemService.save(cartItem);

        // reconvert to dto to get generated field id
        CartItemDTO savedItemDTO = this.cartItemToCartItemDTOConverter.convert(savedItem);
        return ResponseEntity.ok(savedItemDTO);

    }

    @PutMapping("/{userId}/{itemId}")
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Integer userId,
            @PathVariable Integer itemId, @Valid @RequestBody CartItemDTO cartItemDTO){
        CartItemId cartItemId = new CartItemId(userId, itemId);
        CartItem cartItem = this.cartItemDTOToCartItemConverter.convert(cartItemDTO);
        CartItem updatedItem = this.cartItemService.update(cartItemId,cartItem);
        CartItemDTO updatedItemDTO = this.cartItemToCartItemDTOConverter.convert(updatedItem);
        return ResponseEntity.ok(updatedItemDTO);
    }

    @DeleteMapping("/{userId}/{itemId}")
    public ResponseEntity<String> deleteCartItem (@PathVariable Integer userId,
                                              @PathVariable Integer itemId){
        CartItemId cartItemId = new CartItemId(userId, itemId);
        this.cartItemService.delete(cartItemId);
        return ResponseEntity.ok("CartItem deleted successfully!");

    }

    //TODO TEST THIS ENDPOINT
    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteAllByUserId (@PathVariable Integer userId){
        this.cartItemService.deleteAllByUserId(userId);
        return ResponseEntity.ok("CartItems of user: " + userId + " deleted successfully!");
    }

}
