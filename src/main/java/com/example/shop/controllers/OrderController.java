package com.example.shop.controllers;

import com.example.shop.dtos.OrderDTO;
import com.example.shop.dtos.converters.OrderDTOToOrderConverter;
import com.example.shop.dtos.converters.OrderToOrderDTOConverter;
import com.example.shop.models.Order;
import com.example.shop.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderToOrderDTOConverter orderToOrderDTOConverter;
    private final OrderDTOToOrderConverter orderDTOToOrderConverter;



    @GetMapping("")
    public ResponseEntity<List<OrderDTO>> getOrders(){
        List<Order> foundOrders = this.orderService.findAll();
        //convert to dtos
        List<OrderDTO> foundOrdersDTO = foundOrders.stream()
                .map(this.orderToOrderDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundOrdersDTO);

    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> findOrderById(@PathVariable("orderId") int orderId){
        Order foundOrder = this.orderService.findById(orderId);
        return ResponseEntity.ok(this.orderToOrderDTOConverter.convert(foundOrder));
    }



    @PostMapping("")
    //valid checks for validity of fields defined in OrderDto class with annotation
    // request body
    public ResponseEntity<OrderDTO> addOrder(@Valid @RequestBody OrderDTO orderDTO){
        //convert dto to object
        Order order = this.orderDTOToOrderConverter.convert(orderDTO);
        //save order
        Order savedOrder = this.orderService.save(order);
        // reconvert to dto to get generated field id
        OrderDTO savedOrderDTO = this.orderToOrderDTOConverter.convert(savedOrder);
        return ResponseEntity.ok(savedOrderDTO);

    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Integer orderId, @Valid @RequestBody OrderDTO orderDTO){
        Order order = this.orderDTOToOrderConverter.convert(orderDTO);
        Order updatedOrder = this.orderService.update(orderId,order);
        OrderDTO updatedOrderDTO = this.orderToOrderDTOConverter.convert(updatedOrder);
        return ResponseEntity.ok(updatedOrderDTO);
    }

    //TODO: TEST THIS ENDPOINT

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder (@PathVariable Integer orderId){
        this.orderService.delete(orderId);
        return ResponseEntity.ok("Order deleted successfully!");

    }
}
