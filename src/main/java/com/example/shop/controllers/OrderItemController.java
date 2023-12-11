package com.example.shop.controllers;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.dtos.OrderItemRequestDTO;
import com.example.shop.dtos.OrderItemResponseDTO;
import com.example.shop.dtos.converters.OrderItemRequestDTOToOrderItemConverter;
import com.example.shop.dtos.converters.OrderItemResponseDTOToOrderItemConverter;
import com.example.shop.dtos.converters.OrderItemToOrderItemResponseDTOConverter;
import com.example.shop.models.Item;
import com.example.shop.models.Order;
import com.example.shop.models.OrderItem;
import com.example.shop.services.ItemService;
import com.example.shop.services.OrderItemService;
import com.example.shop.services.OrderService;
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
    OrderItemToOrderItemResponseDTOConverter orderItemToOrderItemResponseDTOConverter;
    OrderItemResponseDTOToOrderItemConverter orderItemResponseDTOToOrderItemConverter;
    OrderItemRequestDTOToOrderItemConverter orderItemRequestDTOToOrderItemConverter;
    ItemService itemService;
    OrderService orderService;

    @GetMapping("")
    public ResponseEntity<List<OrderItemResponseDTO>> getOrderItems(){
        List<OrderItem> foundOrderItems = this.orderItemService.findAll();
        //convert to dtos
        List<OrderItemResponseDTO> foundOrderItemsDTO = foundOrderItems.stream()
                .map(this.orderItemToOrderItemResponseDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundOrderItemsDTO);

    }
    @GetMapping("/{orderId}/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> findOrderItemById(@PathVariable("orderId") Integer orderId,
                                                                  @PathVariable("itemId") Integer itemId){
        OrderItemId orderItemId = new OrderItemId(orderId, itemId);
        OrderItem foundOrderItem = this.orderItemService.findById(orderItemId);
        return ResponseEntity.ok(this.orderItemToOrderItemResponseDTOConverter.convert(foundOrderItem));
    }

    @PostMapping("/{orderId}/{itemId}")
    //valid checks for validity of fields defined in ItemDto class with annotation
    // request body takes
    public ResponseEntity<OrderItemResponseDTO> addOrderItem(@Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO,
                                                             @PathVariable("orderId") Integer orderId,
                                                             @PathVariable("itemId") Integer itemId){

        OrderItemId orderItemId = new OrderItemId(orderId, itemId);
        //convert dto to object
        OrderItem orderItem = this.orderItemRequestDTOToOrderItemConverter.convert(orderItemRequestDTO);
        //overwrite orderItemId for security reasons not ideal since prone to errors:
        //in parameters TODO REFACTOR THIS:
        //find items and order to ensure correctness of url parameters (services throws exceptions if not found
        // so orderItem.setId won't return null pointer)
        Item foundItem = this.itemService.findById(itemId);
        Order foundOrder = this.orderService.findById(orderId);
        orderItem.setId(new OrderItemId(orderId,itemId));
        //save orderItem
        OrderItem savedItem = this.orderItemService.save(orderItem);

        OrderItemResponseDTO savedItemDTO = this.orderItemToOrderItemResponseDTOConverter.convert(savedItem);
        return ResponseEntity.ok(savedItemDTO);

    }

    @PutMapping("/{orderId}/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(@PathVariable Integer orderId,
                                                               @PathVariable Integer itemId, @Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO){
        OrderItemId orderItemId = new OrderItemId(orderId, itemId);
        OrderItem orderItem = this.orderItemRequestDTOToOrderItemConverter.convert(orderItemRequestDTO);
        OrderItem updatedItem = this.orderItemService.update(orderItemId,orderItem);
        OrderItemResponseDTO updatedItemDTO = this.orderItemToOrderItemResponseDTOConverter.convert(updatedItem);
        return ResponseEntity.ok(updatedItemDTO);
    }

    @DeleteMapping("/{orderId}/{itemId}")
    public ResponseEntity<String> deleteOrderItem (@PathVariable Integer orderId,
                                                  @PathVariable Integer itemId){
        OrderItemId orderItemId = new OrderItemId(orderId, itemId);
        this.orderItemService.delete(orderItemId);
        return ResponseEntity.ok("OrderItem deleted successfully!");

    }

}
