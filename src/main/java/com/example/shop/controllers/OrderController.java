package com.example.shop.controllers;

import com.example.shop.dtos.OrderRequestDTO;
import com.example.shop.dtos.OrderResponseDTO;
import com.example.shop.dtos.converters.OrderRequestDTOToOrderConverter;
import com.example.shop.dtos.converters.OrderResponseDTOToOrderConverter;
import com.example.shop.dtos.converters.OrderToOrderDTOConverter;
import com.example.shop.models.Order;
import com.example.shop.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderToOrderDTOConverter orderToOrderDTOConverter;
    private final OrderResponseDTOToOrderConverter orderResponseDTOToOrderConverter;
    private final OrderRequestDTOToOrderConverter orderRequestDTOToOrderConverter;



    @GetMapping("")
    public ResponseEntity<List<OrderResponseDTO>> getOrders(){
        List<Order> foundOrders = this.orderService.findAll();
        //convert to dtos
        List<OrderResponseDTO> foundOrdersDTO = foundOrders.stream()
                .map(this.orderToOrderDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundOrdersDTO);

    }
    @GetMapping("/{orderId}")
    public ResponseEntity<?> findOrderById(@PathVariable("orderId") int orderId){
        Order foundOrder = this.orderService.findById(orderId);
        return ResponseEntity.ok(this.orderToOrderDTOConverter.convert(foundOrder));
    }



    @PostMapping("/{userId}")
    //valid checks for validity of fields defined in OrderDto class with annotation
    // request body
    public ResponseEntity<Object> addOrder(@PathVariable("userId") int userId){
        //save order
        Order savedOrder = this.orderService.save(userId);
        //TODO REWORK DTO
//         reconvert to dto to get generated field id
        OrderResponseDTO savedOrderResponseDTO = this.orderToOrderDTOConverter.convert(savedOrder);
        return ResponseEntity.ok(savedOrderResponseDTO);

    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer orderId,
                                         @Valid @RequestBody OrderRequestDTO orderRequestDTO){
        Order order = this.orderRequestDTOToOrderConverter.convert(orderRequestDTO);
        Order updatedOrder = this.orderService.update(orderId,order);
        OrderResponseDTO updatedOrderResponseDTO = this.orderToOrderDTOConverter.convert(updatedOrder);
        return ResponseEntity.ok(updatedOrderResponseDTO);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder (@PathVariable Integer orderId){
        this.orderService.delete(orderId);
        return ResponseEntity.ok("Order deleted successfully!");

    }
}
