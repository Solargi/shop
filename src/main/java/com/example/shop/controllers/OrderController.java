package com.example.shop.controllers;


import com.example.shop.dtos.*;
import com.example.shop.dtos.converters.OrderRequestDTOToOrderConverter;
import com.example.shop.dtos.converters.OrderResponseDTOToOrderConverter;
import com.example.shop.dtos.converters.OrderToOrderDTOConverter;
import com.example.shop.models.CartItem;
import com.example.shop.models.Order;
import com.example.shop.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private final OrderResponseDTOToOrderConverter orderResponseDTOToOrderConverter;
    private final OrderRequestDTOToOrderConverter orderRequestDTOToOrderConverter;


    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "get all orders data",
            description = "get an array containing all orders data (or an empty one if there is no orders), you can get the orders data only if " +
                    "the currently logged in user has role admin",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDTO.class),
                                    examples = {
                                            //TODO CHANGE THE EXAMPLE
                                            @ExampleObject(name = "order items List Example",
                                                    value = "[\n" +
                                                            "{\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"user\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"username\": \"u1\",\n" +
                                                            "        \"name\": \"a\",\n" +
                                                            "        \"surname\": \"b\",\n" +
                                                            "        \"email\": \"c\",\n" +
                                                            "        \"birthDate\": \"yay\",\n" +
                                                            "        \"roles\": \"admin\"\n" +
                                                            "    },\n" +
                                                            "    \"status\": \"aiuto\",\n" +
                                                            "    \"shippingAddress\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"userDTO\": {\n" +
                                                            "            \"id\": 1,\n" +
                                                            "            \"username\": \"u1\",\n" +
                                                            "            \"name\": \"a\",\n" +
                                                            "            \"surname\": \"b\",\n" +
                                                            "            \"email\": \"c\",\n" +
                                                            "            \"birthDate\": \"yay\",\n" +
                                                            "            \"roles\": \"admin\"\n" +
                                                            "        },\n" +
                                                            "        \"country\": \"a\",\n" +
                                                            "        \"state\": \"d\",\n" +
                                                            "        \"city\": \"b\",\n" +
                                                            "        \"street\": \"c\",\n" +
                                                            "        \"zipCode\": 56\n" +
                                                            "    },\n" +
                                                            "    \"paid\": \"true\",\n" +
                                                            "    \"shippingCost\": \"35\",\n" +
                                                            "    \"totalCost\": \"29\"\n" +
                                                            "},\n" +
                                                            "{\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"user\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"username\": \"u1\",\n" +
                                                            "        \"name\": \"a\",\n" +
                                                            "        \"surname\": \"b\",\n" +
                                                            "        \"email\": \"c\",\n" +
                                                            "        \"birthDate\": \"yay\",\n" +
                                                            "        \"roles\": \"admin\"\n" +
                                                            "    },\n" +
                                                            "    \"status\": \"aiuto\",\n" +
                                                            "    \"shippingAddress\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"userDTO\": {\n" +
                                                            "            \"id\": 1,\n" +
                                                            "            \"username\": \"u1\",\n" +
                                                            "            \"name\": \"a\",\n" +
                                                            "            \"surname\": \"b\",\n" +
                                                            "            \"email\": \"c\",\n" +
                                                            "            \"birthDate\": \"yay\",\n" +
                                                            "            \"roles\": \"admin\"\n" +
                                                            "        },\n" +
                                                            "        \"country\": \"a\",\n" +
                                                            "        \"state\": \"d\",\n" +
                                                            "        \"city\": \"b\",\n" +
                                                            "        \"street\": \"c\",\n" +
                                                            "        \"zipCode\": 56\n" +
                                                            "    },\n" +
                                                            "    \"paid\": \"false\",\n" +
                                                            "    \"shippingCost\": \"35\",\n" +
                                                            "    \"totalCost\": \"29\"\n" +
                                                            "}"+
                                                            "]",
                                                    description = "array containing orders data"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "fetching orders while having role user",
                                                    description = "trying to fetch the orders using a JWT with role user, " +
                                                            "will result in an empty forbidden response"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "internal Server Error", content = {
                            @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "some error",
                                                    value = "either empty or a string with the error",
                                                    description = "this response body can contain either the error text or be empty"
                                            ),
                                    }
                            ),
                            @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "empty",
                                                    summary = "empty response"
                                            ),
                                    }
                            )
                    }
                    )
            }
    )
    @GetMapping("")
    public ResponseEntity<List<OrderResponseDTO>> getOrders(){
        List<Order> foundOrders = this.orderService.findAll();
        //convert to dtos
        List<OrderResponseDTO> foundOrdersDTO = foundOrders.stream()
                .map(this.orderToOrderDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundOrdersDTO);

    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getCartItemsByUserId(@PathVariable("userId") Integer userId){
        List<Order> foundOrders = this.orderService.findAllByUserId(userId);
        //convert to dtos
        List<OrderResponseDTO> foundOrdersDTO = foundOrders.stream()
                .map(this.orderToOrderDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundOrdersDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "OrderId", description = "The id of the target order", example = "1")
    })
    @Operation(
            summary = "get order data",
            description = "get order data, you can get the order data only of the currently logged in user" +
                    "unless the logged user has an admin role, admins can get information about a specific order",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDTO.class),
                                    examples = {
                                            @ExampleObject(name = "order data example",
                                                    value =  "{\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"user\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"username\": \"u1\",\n" +
                                                            "        \"name\": \"a\",\n" +
                                                            "        \"surname\": \"b\",\n" +
                                                            "        \"email\": \"c\",\n" +
                                                            "        \"birthDate\": \"yay\",\n" +
                                                            "        \"roles\": \"admin\"\n" +
                                                            "    },\n" +
                                                            "    \"status\": \"aiuto\",\n" +
                                                            "    \"shippingAddress\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"userDTO\": {\n" +
                                                            "            \"id\": 1,\n" +
                                                            "            \"username\": \"u1\",\n" +
                                                            "            \"name\": \"a\",\n" +
                                                            "            \"surname\": \"b\",\n" +
                                                            "            \"email\": \"c\",\n" +
                                                            "            \"birthDate\": \"yay\",\n" +
                                                            "            \"roles\": \"admin\"\n" +
                                                            "        },\n" +
                                                            "        \"country\": \"a\",\n" +
                                                            "        \"state\": \"d\",\n" +
                                                            "        \"city\": \"b\",\n" +
                                                            "        \"street\": \"c\",\n" +
                                                            "        \"zipCode\": 56\n" +
                                                            "    },\n" +
                                                            "    \"paid\": \"true\",\n" +
                                                            "    \"shippingCost\": \"35\",\n" +
                                                            "    \"totalCost\": \"29\"\n" +
                                                            "}",
                                                    description = "order data"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "fetching another user's order while having role user",
                                                    description = "trying to fetch another user's order using a JWT with role user, " +
                                                            "will result in an empty forbidden response"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "order not found in database",
                                                    value = "could not find order with id 1",
                                                    description = "the order with the provided id does not exist"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "internal Server Error", content = {
                            @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "some error",
                                                    value = "either empty or a string with the error",
                                                    description = "this response body can contain either the error text or be empty"
                                            ),
                                    }
                            ),
                            @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "empty",
                                                    summary = "empty response"
                                            ),
                                    }
                            )
                    }
                    )
            }
    )
    @GetMapping("/{orderId}")
    public ResponseEntity<?> findOrderById(@PathVariable("orderId") int orderId){
        Order foundOrder = this.orderService.findById(orderId);
        return ResponseEntity.ok(this.orderToOrderDTOConverter.convert(foundOrder));
    }


    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "userId", description = "The id of the target user", example = "1")
    })
    @Operation(
            summary = "place order",
            description = "place an order for the user with the target id, you can place the order only of the currently logged in user" +
                    "unless the logged user has an admin role, admins can place an order for any user",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDTO.class),
                                    examples = {
                                            @ExampleObject(name = "order data example",
                                                    value =  "{\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"user\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"username\": \"u1\",\n" +
                                                            "        \"name\": \"a\",\n" +
                                                            "        \"surname\": \"b\",\n" +
                                                            "        \"email\": \"c\",\n" +
                                                            "        \"birthDate\": \"yay\",\n" +
                                                            "        \"roles\": \"admin\"\n" +
                                                            "    },\n" +
                                                            "    \"status\": \"aiuto\",\n" +
                                                            "    \"shippingAddress\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"userDTO\": {\n" +
                                                            "            \"id\": 1,\n" +
                                                            "            \"username\": \"u1\",\n" +
                                                            "            \"name\": \"a\",\n" +
                                                            "            \"surname\": \"b\",\n" +
                                                            "            \"email\": \"c\",\n" +
                                                            "            \"birthDate\": \"yay\",\n" +
                                                            "            \"roles\": \"admin\"\n" +
                                                            "        },\n" +
                                                            "        \"country\": \"a\",\n" +
                                                            "        \"state\": \"d\",\n" +
                                                            "        \"city\": \"b\",\n" +
                                                            "        \"street\": \"c\",\n" +
                                                            "        \"zipCode\": 56\n" +
                                                            "    },\n" +
                                                            "    \"paid\": \"true\",\n" +
                                                            "    \"shippingCost\": \"35\",\n" +
                                                            "    \"totalCost\": \"29\"\n" +
                                                            "}",
                                                    description = "order data"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "no shipping address",
                                                    value = "User doesn't have a shipping address",
                                                    description = "the user has no associated address"
                                            ),
                                            @ExampleObject(
                                                    name = "order quantity error",
                                                    value = "no orderItems found in the order. Check available quantities before placing orders",
                                                    description = "could not create orderItems for this order, this is probably due to " +
                                                            "unavailable/wrong quantities in the user's cart (items with invalid quantities" +
                                                            "won't be included in the order and will be left in the user's cart)"
                                            ),
                                            @ExampleObject(
                                                    name = "empty cart",
                                                    value = "User's cart has no items in it",
                                                    description = "the user has no associated CartItems"
                                            ),
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "placing another user's order while having role user",
                                                    description = "trying to place another user's order using a JWT with role user, " +
                                                            "will result in an empty forbidden response"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "user not found in database",
                                                    value = "could not find user with id 1",
                                                    description = "the user with the provided id does not exist"
                                            ),
                                            @ExampleObject(
                                                    name = "shipping address not found in database",
                                                    value = "could not find address with id 1",
                                                    description = "the user's address doesn't exist"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "internal Server Error", content = {
                            @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "some error",
                                                    value = "either empty or a string with the error",
                                                    description = "this response body can contain either the error text or be empty"
                                            ),
                                    }
                            ),
                            @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "empty",
                                                    summary = "empty response"
                                            ),
                                    }
                            )
                    }
                    )
            }
    )

    @PostMapping("/{userId}")
    //valid checks for validity of fields defined in OrderDto class with annotation
    // request body
    public ResponseEntity<Object> addOrder(@PathVariable("userId") int userId){
        //save order
        Order savedOrder = this.orderService.save(userId);
//         reconvert to dto to get generated field id
        OrderResponseDTO savedOrderResponseDTO = this.orderToOrderDTOConverter.convert(savedOrder);
        return ResponseEntity.ok(savedOrderResponseDTO);

    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "orderId", description = "The id of the target order", example = "1")
    })
    @Operation(
            summary = "update order",
            description = "update the order with the target id, you can update an order only if" +
                    "the logged user has an admin role admin",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequestDTO.class),
                            examples = {
                                    @ExampleObject(name = "Order 1",
                                            value = "{\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"status\": \"aiuto\",\n" +
                                                    "    \"shippingAddress\": {\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"userDTO\": {\n" +
                                                    "            \"id\": 1,\n" +
                                                    "            \"username\": \"u1\",\n" +
                                                    "            \"name\": \"a\",\n" +
                                                    "            \"surname\": \"b\",\n" +
                                                    "            \"email\": \"c\",\n" +
                                                    "            \"birthDate\": \"yay\",\n" +
                                                    "            \"roles\": \"admin\"\n" +
                                                    "        },\n" +
                                                    "        \"country\": \"a\",\n" +
                                                    "        \"state\": \"d\",\n" +
                                                    "        \"city\": \"b\",\n" +
                                                    "        \"street\": \"c\",\n" +
                                                    "        \"zipCode\": 56\n" +
                                                    "    },\n" +
                                                    "    \"paid\": \"true\",\n" +
                                                    "    \"shippingCost\": \"35\",\n" +
                                                    "    \"totalCost\": \"29\"\n" +
                                                    "}",
                                            description = "update order with the target id"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDTO.class),
                                    examples = {
                                            @ExampleObject(name = "order data example",
                                                    value =  "{\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"user\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"username\": \"u1\",\n" +
                                                            "        \"name\": \"a\",\n" +
                                                            "        \"surname\": \"b\",\n" +
                                                            "        \"email\": \"c\",\n" +
                                                            "        \"birthDate\": \"yay\",\n" +
                                                            "        \"roles\": \"admin\"\n" +
                                                            "    },\n" +
                                                            "    \"status\": \"aiuto\",\n" +
                                                            "    \"shippingAddress\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"userDTO\": {\n" +
                                                            "            \"id\": 1,\n" +
                                                            "            \"username\": \"u1\",\n" +
                                                            "            \"name\": \"a\",\n" +
                                                            "            \"surname\": \"b\",\n" +
                                                            "            \"email\": \"c\",\n" +
                                                            "            \"birthDate\": \"yay\",\n" +
                                                            "            \"roles\": \"admin\"\n" +
                                                            "        },\n" +
                                                            "        \"country\": \"a\",\n" +
                                                            "        \"state\": \"d\",\n" +
                                                            "        \"city\": \"b\",\n" +
                                                            "        \"street\": \"c\",\n" +
                                                            "        \"zipCode\": 56\n" +
                                                            "    },\n" +
                                                            "    \"paid\": \"true\",\n" +
                                                            "    \"shippingCost\": \"35\",\n" +
                                                            "    \"totalCost\": \"29\"\n" +
                                                            "}",
                                                    description = "order data"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "order quantity error",
                                                    value = "no orderItems found in the order. Check available quantities before placing orders",
                                                    description = "could not create orderItems for this order, this is probably due to " +
                                                            "unavailable/wrong quantities in the user's cart (items with invalid quantities" +
                                                            "won't be included in the order and will be left in the user's cart)"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "updating an order without role admin",
                                                    description = "trying to update an order using a JWT with role user, " +
                                                            "will result in an empty forbidden response. Only admins " +
                                                            "can modify an order"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "order not found in database",
                                                    value = "could not find order with id 1",
                                                    description = "the order with the provided id does not exist"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "internal Server Error", content = {
                            @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "some error",
                                                    value = "either empty or a string with the error",
                                                    description = "this response body can contain either the error text or be empty"
                                            ),
                                    }
                            ),
                            @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "empty",
                                                    summary = "empty response"
                                            ),
                                    }
                            )
                    }
                    )
            }
    )
    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer orderId,
                                         @Valid @RequestBody OrderRequestDTO orderRequestDTO){
        Order order = this.orderRequestDTOToOrderConverter.convert(orderRequestDTO);
        Order updatedOrder = this.orderService.update(orderId,order);
        OrderResponseDTO updatedOrderResponseDTO = this.orderToOrderDTOConverter.convert(updatedOrder);
        return ResponseEntity.ok(updatedOrderResponseDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "orderId", description = "The id of the target order", example = "1")
    })
    @Operation(
            summary = "delete order",
            description = "delete the order with the target id, you can delete an order only if" +
                    "the logged user has an admin role admin",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(name = "successful delete",
                                                    value = "order deleted successfully!"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "updating an order without role admin",
                                                    description = "trying to delete an order using a JWT with role user, " +
                                                            "will result in an empty forbidden response. Only admins " +
                                                            "can delete an order"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "order not found in database",
                                                    value = "could not find order with id 1",
                                                    description = "the order with the provided id does not exist"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "internal Server Error", content = {
                            @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "some error",
                                                    value = "either empty or a string with the error",
                                                    description = "this response body can contain either the error text or be empty"
                                            ),
                                    }
                            ),
                            @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "empty",
                                                    summary = "empty response"
                                            ),
                                    }
                            )
                    }
                    )
            }
    )
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder (@PathVariable Integer orderId){
        this.orderService.delete(orderId);
        return ResponseEntity.ok("Order deleted successfully!");
        //TODO restore items quantity before deleting order items

    }
}
