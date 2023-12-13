package com.example.shop.controllers;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.dtos.CartItemRequestDTO;
import com.example.shop.dtos.CartItemResponseDTO;
import com.example.shop.dtos.converters.CartItemRequestDTOToCartItemConverter;
import com.example.shop.dtos.converters.CartItemResponseDTOToCartItemConverter;
import com.example.shop.dtos.converters.CartItemToCartItemResponseDTOConverter;
import com.example.shop.models.CartItem;
import com.example.shop.models.User;
import com.example.shop.services.CartItemService;
import com.example.shop.services.UserService;
import com.example.shop.system.exceptions.GenericException;
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
    CartItemToCartItemResponseDTOConverter cartItemToCartItemResponseDTOConverter;
    CartItemRequestDTOToCartItemConverter cartItemRequestDTOToCartItemConverter;
    @GetMapping("")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItems(){
        List<CartItem> foundCartItems = this.cartItemService.findAll();
        //convert to dtos
        List<CartItemResponseDTO> foundItemsDTO = foundCartItems.stream()
                .map(this.cartItemToCartItemResponseDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundItemsDTO);

    }
    @GetMapping("/{userId}/{itemId}")
    public ResponseEntity<CartItemResponseDTO> findCartItemById(@PathVariable("userId") Integer userId,
                                                                @PathVariable("itemId") Integer itemId){
        CartItemId cartItemId = new CartItemId(userId, itemId);
        CartItem foundCartItem = this.cartItemService.findById(cartItemId);
        return ResponseEntity.ok(this.cartItemToCartItemResponseDTOConverter.convert(foundCartItem));
    }

    //TODO: ADD A TEST ENDPOINT TO FETCH ALL CARTITEMS BELONGING TO A USER USING USERID

    @PostMapping("/{userId}/{itemId}")
    //valid checks for validity of fields defined in ItemDto class with annotation
    // request body takes
    public ResponseEntity<CartItemResponseDTO> addCartItem(@Valid @RequestBody CartItemRequestDTO cartItemRequestDTO,
                                                           @PathVariable Integer userId,
                                                           @PathVariable Integer itemId){
        //convert dto to object
        CartItem cartItem = this.cartItemRequestDTOToCartItemConverter.convert(cartItemRequestDTO, userId, itemId);

        //save cartItem
        CartItem savedItem = this.cartItemService.save(cartItem);

        // reconvert to response dto to have all valid fields(user dto, item dto,...)
        CartItemResponseDTO savedItemDTO = this.cartItemToCartItemResponseDTOConverter.convert(savedItem);
        return ResponseEntity.ok(savedItemDTO);

    }

    @PutMapping("/{userId}/{itemId}")
    public ResponseEntity<CartItemResponseDTO> updateCartItem(@PathVariable Integer userId,
                                                              @PathVariable Integer itemId, @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO){
        CartItem cartItem = this.cartItemRequestDTOToCartItemConverter.convert(cartItemRequestDTO, userId, itemId);

        //service defines missing fields in requestItem
        CartItem updatedItem = this.cartItemService.update(cartItem);
        CartItemResponseDTO updatedItemDTO = this.cartItemToCartItemResponseDTOConverter.convert(updatedItem);
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
