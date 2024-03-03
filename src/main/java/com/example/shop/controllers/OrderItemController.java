package com.example.shop.controllers;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.dtos.*;
import com.example.shop.dtos.converters.OrderItemRequestDTOToOrderItemConverter;
import com.example.shop.dtos.converters.OrderItemResponseDTOToOrderItemConverter;
import com.example.shop.dtos.converters.OrderItemToOrderItemResponseDTOConverter;
import com.example.shop.models.Item;
import com.example.shop.models.Order;
import com.example.shop.models.OrderItem;
import com.example.shop.services.ItemService;
import com.example.shop.services.OrderItemService;
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
@RequestMapping("${api.endpoint.base-url}/orderItems")
@AllArgsConstructor
public class
OrderItemController {
    OrderItemService orderItemService;
    OrderItemToOrderItemResponseDTOConverter orderItemToOrderItemResponseDTOConverter;
    OrderItemResponseDTOToOrderItemConverter orderItemResponseDTOToOrderItemConverter;
    OrderItemRequestDTOToOrderItemConverter orderItemRequestDTOToOrderItemConverter;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "get all oderItem data",
            description = "get an array containing all orderItems data (or an empty one if there is no orderItems), you can get the orderItems data only if " +
                    "the currently logged in user has role admin",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDTO.class),
                                    examples = {
                                            @ExampleObject(name = "orderItems List Example",
                                                    value = "[\n" +
                                                                "{\n" +
                                                                     "\"orderResponseDTO\" : {\n" +
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
                                                                     "\"itemDTO\" : {\n" +
                                                                     "        \"id\": 1,\n" +
                                                                     "        \"name\": \"i1\",\n" +
                                                                     "        \"description\": \"something\",\n" +
                                                                     "        \"price\": \"13\",\n" +
                                                                     "        \"availableQuantity\": \"4\"\n" +
                                                                     "    },\n" +
                                                                     "\"quantity\": \"2\",\n" +
                                                                     "\"totalCost\": \"15\"\n" +
                                                            "}, \n" +
                                                            "{\n" +
                                                            "\"orderResponseDTO\" : {\n" +
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
                                                            "\"itemDTO\" : {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": \"4\"\n" +
                                                            "    },\n" +
                                                            "\"quantity\": \"2\",\n" +
                                                            "\"totalCost\": \"15\"\n" +
                                                            "}" +
                                                            "]",
                                                    description = "array containing oderItems data"
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
                                                    name = "fetching oderItem while having role user",
                                                    description = "trying to fetch the oderItem using a JWT with role user, " +
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
    public ResponseEntity<List<OrderItemResponseDTO>> getOrderItems(){
        List<OrderItem> foundOrderItems = this.orderItemService.findAll();
        //convert to dtos
        List<OrderItemResponseDTO> foundOrderItemsDTO = foundOrderItems.stream()
                .map(this.orderItemToOrderItemResponseDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundOrderItemsDTO);

    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "orderId", description = "The id of the target order", example = "1"),
            @Parameter(name = "itemId", description = "The id of the target item", example = "1")
    })
    @Operation(
            summary = "get orderItem data",
            description = "get orderItem data, you can get the orderItem data only of the currently logged in user" +
                    "unless the logged user has an admin role, admins can get information about any specific orderItem",

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
                                                            "\"orderResponseDTO\" : {\n" +
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
                                                            "\"itemDTO\" : {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": \"4\"\n" +
                                                            "    },\n" +
                                                            "\"quantity\": \"2\",\n" +
                                                            "\"totalCost\": \"15\"\n" +
                                                            "}",
                                                    description = "orderItem data"
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
                                                    name = "fetching another user's orderItem while having role user",
                                                    description = "trying to fetch another user's orderItem using a JWT with role user, " +
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
                                            ),
                                            @ExampleObject(
                                                    name = "Item not found in database",
                                                    value = "could not find item with id 1",
                                                    description = "the item with the provided id does not exist"
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

    @GetMapping("/{orderId}/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> findOrderItemById(@PathVariable("orderId") Integer orderId,
                                                                  @PathVariable("itemId") Integer itemId){
        OrderItemId orderItemId = new OrderItemId(orderId, itemId);
        OrderItem foundOrderItem = this.orderItemService.findById(orderItemId);
        return ResponseEntity.ok(this.orderItemToOrderItemResponseDTOConverter.convert(foundOrderItem));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "orderId", description = "The id of the target order", example = "1"),
            @Parameter(name = "itemId", description = "The id of the target item", example = "1")
    })

    @Operation(
            summary = "Create a new orderItem",
            description = "an OrderItem can be created and associated to an order only if the currently logged in " +
                   "has role admin",

            // old description:
            // "an OrderItem can be created and associated only to an order owned by the currently logged in " +
            //"user, unless the user has role admin, admins can create orderItems associated to any order",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemRequestDTO.class),
                            examples = {
                                    @ExampleObject(name = "OrderItemRequest 1",
                                            value = "{\n" +
                                                    "    \"quantity\": 4,\n" +
                                                    "}",
                                            description = "create an orderItem associated to the order" +
                                                    " having id 1 and to the item having id 1"
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
                                    schema = @Schema(implementation = AddressDTO.class),
                                    examples = {
                                            @ExampleObject(name = "orderItem data example",
                                                    value =  "{\n" +
                                                            "\"orderResponseDTO\" : {\n" +
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
                                                            "\"itemDTO\" : {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": \"4\"\n" +
                                                            "    },\n" +
                                                            "\"quantity\": \"2\",\n" +
                                                            "\"totalCost\": \"15\"\n" +
                                                            "}",
                                                    description = "orderItem data"
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
                                                    name = "missing or empty requiered fields",
                                                    value = "{\n" +
                                                            "    \"quantity\": \"must not be null\",\n" +
                                                            "    \"quantity\": \"must be greater than 0\",\n" +
                                                            "}",
                                                    description = "(the response can either be empty or contain one or more of the listed elements)"
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
                                                    name = "posting orderItem while having role user",
                                                    description = "trying to post an orderItem using a JWT with role user, " +
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
                                                    name = "order not found",
                                                    value = "could not find order with id 1",
                                                    description = "the order with the provided id does not exist"
                                            ),
                                            @ExampleObject(
                                                    name = "item not found",
                                                    value = "could not find item with id 1",
                                                    description = "the item with the provided id does not exist"
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

    //TODO: ADD ISSUE ON GIT TO ADD CHECK FOR ITEM QUANTITY FOR POST AND PUT

    @PostMapping("/{orderId}/{itemId}")
    //valid checks for validity of fields defined in ItemDto class with annotation
    // request body takes
    public ResponseEntity<OrderItemResponseDTO> addOrderItem(@Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO,
                                                             @PathVariable("orderId") Integer orderId,
                                                             @PathVariable("itemId") Integer itemId){
        //convert dto to object
        OrderItem orderItem = this.orderItemRequestDTOToOrderItemConverter.convert(orderItemRequestDTO, orderId, itemId);

        //save orderItem
        OrderItem savedItem = this.orderItemService.save(orderItem);

        OrderItemResponseDTO savedItemDTO = this.orderItemToOrderItemResponseDTOConverter.convert(savedItem);
        return ResponseEntity.ok(savedItemDTO);

    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "orderId", description = "The id of the target order", example = "1"),
            @Parameter(name = "itemId", description = "The id of the target item", example = "1")
    })

    @Operation(
            summary = "update an orderItem",
            description = "an OrderItem can be updated only if the currently logged in " +
                    "has role admin",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemRequestDTO.class),
                            examples = {
                                    @ExampleObject(name = "OrderItemRequest 1",
                                            value = "{\n" +
                                                    "\"quantity\": 4\n" +
                                                    "}",
                                            description = "update an orderItem associated to the order" +
                                                    " having id 1 and to the item having id 1"
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
                                    schema = @Schema(implementation = AddressDTO.class),
                                    examples = {
                                            @ExampleObject(name = "orderItem data example",
                                                    value =  "{\n" +
                                                            "\"orderResponseDTO\" : {\n" +
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
                                                            "\"itemDTO\" : {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": \"4\"\n" +
                                                            "    },\n" +
                                                            "\"quantity\": \"2\",\n" +
                                                            "\"totalCost\": \"15\"\n" +
                                                            "}",
                                                    description = "orderItem data"
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
                                                    name = "missing or empty requiered fields",
                                                    value = "{\n" +
                                                            "    \"quantity\": \"must not be null\",\n" +
                                                            "    \"quantity\": \"must be greater than 0\",\n" +
                                                            "}",
                                                    description = "(the response can either be empty or contain one or more of the listed elements)"
                                            ),
                                            @ExampleObject(
                                                    name = "Item quantity is lower than ordered quantity",
                                                    value = "{\n" +
                                                            "    \"quantity\": \"must not be null\",\n" +
                                                            "    \"quantity\": \"must be greater than 0\",\n" +
                                                            "}",
                                                    description = "(the response can either be empty or contain one or more of the listed elements)"
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
                                                    name = "updating orderItem while having role user",
                                                    description = "trying to update an orderItem using a JWT with role user, " +
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
                                                    name = "orderItem not found",
                                                    value = "could not find orderItem with id 1",
                                                    description = "the orderItem with the provided id does not exist"
                                            ),
                                            @ExampleObject(
                                                    name = "order not found",
                                                    value = "could not find order with id 1",
                                                    description = "the order with the provided id does not exist"
                                            ),
                                            @ExampleObject(
                                                    name = "item not found",
                                                    value = "could not find item with id 1",
                                                    description = "the item with the provided id does not exist"
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

    @PutMapping("/{orderId}/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(@PathVariable Integer orderId,
                                                               @PathVariable Integer itemId, @Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO){
        OrderItem orderItem = this.orderItemRequestDTOToOrderItemConverter.convert(orderItemRequestDTO,orderId,itemId);
        OrderItem updatedItem = this.orderItemService.update(orderItem);
        OrderItemResponseDTO updatedItemDTO = this.orderItemToOrderItemResponseDTOConverter.convert(updatedItem);
        return ResponseEntity.ok(updatedItemDTO);
    }


    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "orderId", description = "The id of the target order", example = "1"),
            @Parameter(name = "itemId", description = "The id of the target item", example = "1")
    })

    @Operation(
            summary = "delete an orderItem",
            description = "an OrderItem can be deleted only if the currently logged in " +
                    "has role admin",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "text",
                                    schema = @Schema(implementation = AddressDTO.class),
                                    examples = {
                                            @ExampleObject(name = "successful delete",
                                                    value = "orderItem deleted successfully!"
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
                                                    name = "deleting orderItem while having role user",
                                                    description = "trying to update an orderItem using a JWT with role user, " +
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
                                                    name = "orderItem not found",
                                                    value = "could not find orderItem with id 1",
                                                    description = "the orderItem with the provided id does not exist"
                                            ),
                                            @ExampleObject(
                                                    name = "order not found",
                                                    value = "could not find order with id 1",
                                                    description = "the order with the provided id does not exist"
                                            ),
                                            @ExampleObject(
                                                    name = "item not found",
                                                    value = "could not find item with id 1",
                                                    description = "the item with the provided id does not exist"
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
    @DeleteMapping("/{orderId}/{itemId}")
    public ResponseEntity<String> deleteOrderItem (@PathVariable Integer orderId,
                                                  @PathVariable Integer itemId){
        OrderItemId orderItemId = new OrderItemId(orderId, itemId);
        this.orderItemService.delete(orderItemId);
        return ResponseEntity.ok("OrderItem deleted successfully!");
        //TODO delete also order if empty

    }

}
