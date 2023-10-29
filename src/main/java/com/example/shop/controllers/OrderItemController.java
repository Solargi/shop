package com.example.shop.controllers;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.dtos.OrderItemDTO;
import com.example.shop.dtos.OrderItemDTO;
import com.example.shop.dtos.converters.OrderItemDTOToOrderItemConverter;
import com.example.shop.dtos.converters.OrderItemToOrderItemDTOConverter;
import com.example.shop.models.OrderItem;
import com.example.shop.services.OrderItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/orderItems")
@AllArgsConstructor
public class OrderItemController {
    OrderItemService orderItemService;
    OrderItemToOrderItemDTOConverter orderItemToOrderItemDTOConverter;
    OrderItemDTOToOrderItemConverter orderItemDTOToOrderItemConverter;

    @GetMapping("")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(){
        List<OrderItem> foundOrderItems = this.orderItemService.findAll();
        //convert to dtos
        List<OrderItemDTO> foundOrderItemsDTO = foundOrderItems.stream()
                .map(this.orderItemToOrderItemDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundOrderItemsDTO);

    }
    @GetMapping("/{orderId}/{itemId}")
    public ResponseEntity<OrderItemDTO> findOrderItemById(@PathVariable("orderId") Integer userId,
                                                        @PathVariable("itemId") Integer itemId){
        OrderItemId orderItemId = new OrderItemId(userId, itemId);
        OrderItem foundCartItem = this.orderItemService.findById(orderItemId);
        return ResponseEntity.ok(this.orderItemToOrderItemDTOConverter.convert(foundCartItem));
    }

    @PostMapping("")
    //valid checks for validity of fields defined in ItemDto class with annotation
    // request body takes
    public ResponseEntity<OrderItemDTO> addCartItem(@Valid @RequestBody OrderItemDTO orderItemDTO){
        //convert dto to object
        OrderItem orderItem = this.orderItemDTOToOrderItemConverter.convert(orderItemDTO);
        //save orderItem
        OrderItem savedItem = this.orderItemService.save(orderItem);

        OrderItemDTO savedItemDTO = this.orderItemToOrderItemDTOConverter.convert(savedItem);
        return ResponseEntity.ok(savedItemDTO);

    }

    @PutMapping("/{userId}/{itemId}")
    public ResponseEntity<OrderItemDTO> updateCartItem(@PathVariable Integer userId,
                                                      @PathVariable Integer itemId, @Valid @RequestBody OrderItemDTO orderItemDTO){
        OrderItemId orderItemId = new OrderItemId(userId, itemId);
        OrderItem orderItem = this.orderItemDTOToOrderItemConverter.convert(orderItemDTO);
        OrderItem updatedItem = this.orderItemService.update(orderItemId,orderItem);
        OrderItemDTO updatedItemDTO = this.orderItemToOrderItemDTOConverter.convert(updatedItem);
        return ResponseEntity.ok(updatedItemDTO);
    }

    @DeleteMapping("/{orderId}/{itemId}")
    public ResponseEntity<String> deleteCartItem (@PathVariable Integer orderId,
                                                  @PathVariable Integer itemId){
        OrderItemId orderItemId = new OrderItemId(orderId, itemId);
        this.orderItemService.delete(orderItemId);
        return ResponseEntity.ok("OrderItem deleted successfully!");

    }

}
