package com.example.shop.controllers;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.dtos.CartItemRequestDTO;
import com.example.shop.dtos.CartItemResponseDTO;
import com.example.shop.dtos.UserDTO;
import com.example.shop.dtos.converters.CartItemRequestDTOToCartItemConverter;
import com.example.shop.dtos.converters.CartItemToCartItemResponseDTOConverter;
import com.example.shop.models.CartItem;
import com.example.shop.services.CartItemService;
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
@RequestMapping("${api.endpoint.base-url}/cartItems")
@AllArgsConstructor
public class CartItemController {
    CartItemService cartItemService;
    CartItemToCartItemResponseDTOConverter cartItemToCartItemResponseDTOConverter;
    CartItemRequestDTOToCartItemConverter cartItemRequestDTOToCartItemConverter;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "get all cartItems data",
            description = "get an array containing all cartItems data (or an empty one if there is no cartItems), you can get the cartItems data only if " +
                    "the currently logged in user has role admin",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartItemResponseDTO.class),
                                    examples = {
                                            //TODO CHANGE THE EXAMPLE
                                            @ExampleObject(name = "cartItems List Example",
                                                    value = "[\n" +
                                                            "{\n" +
                                                            " \"id\": {\"userId\": 1, \"itemId\" : 1},\n"+
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "\"ItemDTO\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": 4" +
                                                            "},\n" +
                                                            "  \"quantity\": 2,\n" +
                                                            "  \"totalCost\": 4\n" +
                                                            "},\n" +
                                                            "{\n" +
                                                            " \"id\": {\"userId\": 1, \"itemId\" : 1},\n"+
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "\"ItemDTO\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": 4" +
                                                            "},\n" +
                                                            "  \"quantity\": 2,\n" +
                                                            "  \"totalCost\": 4\n" +
                                                            "}\n" +
                                                            "]",
                                                    description = "array containing cartItems data"
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
                                                    name = "fetching cartItems while having role user",
                                                    description = "trying to fetch the cartItems using a JWT with role user, " +
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
    public ResponseEntity<List<CartItemResponseDTO>> getCartItems(){
        List<CartItem> foundCartItems = this.cartItemService.findAll();
        //convert to dtos
        List<CartItemResponseDTO> foundItemsDTO = foundCartItems.stream()
                .map(this.cartItemToCartItemResponseDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundItemsDTO);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItemsByUserId(@PathVariable("userId") Integer userId){
        List<CartItem> foundCartItems = this.cartItemService.findAllByUserId(userId);
        //convert to dtos
        List<CartItemResponseDTO> foundItemsDTO = foundCartItems.stream()
                .map(this.cartItemToCartItemResponseDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundItemsDTO);

    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "userId", description = "The target userId", example = "1"),
            @Parameter(name = "itemId", description = "The target itemId", example = "1")
    })
    @Operation(
            summary = "get CartItem data",
            description = "get cartItem data, you can get the cartItem data only of the currently logged in user" +
                    "unless the logged user has an admin role, admins can get information about any specific cartItem",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartItemResponseDTO.class),
                                    examples = {
                                            @ExampleObject(name = "cartItem data example",
                                                    value = "{\n" +
                                                            " \"id\": {\"userId\": 1, \"itemId\" : 1},\n"+
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "\"ItemDTO\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": 4" +
                                                            "},\n" +
                                                            "  \"quantity\": 2,\n" +
                                                            "  \"totalCost\": 4\n" +
                                                            "}\n",
                                                    description = "cartItem data"
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
                                                    name = "fetching another user's cartItem while having role user",
                                                    description = "trying to fetch another user's cartItem using a JWT with role user, " +
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
                                                    name = "CartItem not found in database",
                                                    value = "could not find cartItem with id 1",
                                                    description = "the cartItem with the provided id does not exist"
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
    @GetMapping("/{userId}/{itemId}")
    public ResponseEntity<CartItemResponseDTO> findCartItemById(@PathVariable("userId") Integer userId,
                                                                @PathVariable("itemId") Integer itemId){
        CartItemId cartItemId = new CartItemId(userId, itemId);
        CartItem foundCartItem = this.cartItemService.findById(cartItemId);
        return ResponseEntity.ok(this.cartItemToCartItemResponseDTOConverter.convert(foundCartItem));
    }

    //TODO: ADD A TEST ENDPOINT TO FETCH ALL CARTITEMS BELONGING TO A USER USING USERID

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "userId", description = "The target userId", example = "1"),
            @Parameter(name = "itemId", description = "The target itemId", example = "1")
    })
    @Operation(
            summary = "Create a new cartItem",
            description = "a cartItem can be created and associated only to the currently logged in " +
                    "user, unless the user has role admin, admins can create cartItems associated to any user." +
                    "To create a cartItem the itemAvailable quantity must me greater than 0 and less than the" +
                    " quantity in the cartItem.",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CartItemRequestDTO.class),
                            examples = {
                                    @ExampleObject(name = "CartItem 1",
                                            value = "{\n" +
                                                    "  \"quantity\": 2 \n}",
                                            description = "create a cartItem associated to the user" +
                                                    " having the provided UserID, and the item having" +
                                                    " the provided itemId"
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
                                    schema = @Schema(implementation = CartItemResponseDTO.class),
                                    examples = {
                                            @ExampleObject(name = "cartItem data example",
                                                    value = "{\n" +
                                                            " \"id\": {\"userId\": 1, \"itemId\" : 1},\n"+
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "\"ItemDTO\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": 4" +
                                                            "},\n" +
                                                            "  \"quantity\": 2,\n" +
                                                            "  \"totalCost\": 4\n" +
                                                            "}\n",
                                                    description = "CartItem data"
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
                                                            "    \"quantity\": \"must not be empty\",\n" +
                                                            "    \"quantity\": \"must be greater than 0\"\n"+
                                                            "}",
                                                    description = "(the response can either be empty or contain one or more of the listed elements)"
                                            ),
                                            @ExampleObject(
                                                    name = "cart item quantity exceeds item's available quantity",
                                                    value = "requested item quantity: 5 available item quantity is 4.00 select lower quantity",
                                                    description = "the quantity requested for user's cart was higher than the item's available quantity"
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
                                                    name = "posting CartItems while having role user",
                                                    description = "trying to post a cartItem for another user using a JWT with role user, " +
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
                                                    name = "item not found in database",
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

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "userId", description = "The target userId", example = "1"),
            @Parameter(name = "itemId", description = "The target itemId", example = "1")
    })
    @Operation(
            summary = "modify an existing cartItem",
            description = "a cartItem can be modified if associated only to the currently logged in " +
                    "user, unless the user has role admin, admins can modify cartItems associated to any user." +
                    "To modify a cartItem the itemAvailable quantity must me greater than 0 and less than the" +
                    " quantity in the cartItem.",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CartItemRequestDTO.class),
                            examples = {
                                    @ExampleObject(name = "CartItem 1",
                                            value = "{\n" +
                                                    "  \"quantity\": 2 \n}",
                                            description = "modify a cartItem associated to the user" +
                                                    " having the provided UserID, and the item having" +
                                                    " the provided itemId"
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
                                    schema = @Schema(implementation = CartItemResponseDTO.class),
                                    examples = {
                                            @ExampleObject(name = "cartItem data example",
                                                    value = "{\n" +
                                                            " \"id\": {\"userId\": 1, \"itemId\" : 1},\n"+
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "\"ItemDTO\": {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": 4" +
                                                            "},\n" +
                                                            "  \"quantity\": 2,\n" +
                                                            "  \"totalCost\": 4\n" +
                                                            "}\n",
                                                    description = "CartItem data"
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
                                                            "    \"quantity\": \"must not be empty\",\n" +
                                                            "    \"quantity\": \"must be greater than 0\"\n"+
                                                            "}",
                                                    description = "(the response can either be empty or contain one or more of the listed elements)"
                                            ),
                                            @ExampleObject(
                                                    name = "cart item quantity exceeds item's available quantity",
                                                    value = "requested item quantity: 5 available item quantity is 4.00 select lower quantity",
                                                    description = "the quantity requested for user's cart was higher than the item's available quantity"
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
                                                    name = "modify a CartItem while having role user",
                                                    description = "trying to modify a cartItem for another user using a JWT with role user, " +
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
                                                    name = "item not found in database",
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
    @PutMapping("/{userId}/{itemId}")
    public ResponseEntity<CartItemResponseDTO> updateCartItem(@PathVariable Integer userId,
                                                              @PathVariable Integer itemId, @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO){
        CartItem cartItem = this.cartItemRequestDTOToCartItemConverter.convert(cartItemRequestDTO, userId, itemId);

        //service defines missing fields in requestItem
        CartItem updatedItem = this.cartItemService.update(cartItem);
        CartItemResponseDTO updatedItemDTO = this.cartItemToCartItemResponseDTOConverter.convert(updatedItem);
        return ResponseEntity.ok(updatedItemDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "userId", description = "The target userId", example = "1"),
            @Parameter(name = "itemId", description = "The target itemId", example = "1")
    })
    @Operation(
            summary = "Delete cartItem",
            description = "you can only delete the currently logged in user's cartItem unless the logged in users has " +
                    "role admin, admins can delete all cartItems. trying to delete another user's cartItem with an account having role user" +
                    "will cause a 403 forbidden response.",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = UserDTO.class),
                                    examples = {
                                            @ExampleObject(name = "successful delete",
                                                    value = "cartItem deleted successfully!"
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
                                                    name = "not admin or unauthorized",
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
                                                    name = "deleting another user's cartItem while having role user",
                                                    description = "trying to delete another user's cartItem using a JWT with role user, " +
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
                                                    name = "cartItem not found in database",
                                                    value = "could not find cartItem with id 1",
                                                    description = "the user with the provided id does not exist"
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
    @DeleteMapping("/{userId}/{itemId}")
    public ResponseEntity<String> deleteCartItem (@PathVariable Integer userId,
                                              @PathVariable Integer itemId){
        CartItemId cartItemId = new CartItemId(userId, itemId);
        this.cartItemService.delete(cartItemId);
        return ResponseEntity.ok("CartItem deleted successfully!");

    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "userId", description = "The target userId", example = "1"),
            @Parameter(name = "itemId", description = "The target itemId", example = "1")
    })

    @Operation(
            summary = "Delete all users's cartItems",
            description = "you can only delete the currently logged in user's cartItems unless the logged in users has " +
                    "role admin, admins can delete all cartItems. trying to delete another user's cartItems with an account having role user" +
                    "will cause a 403 forbidden response.",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = UserDTO.class),
                                    examples = {
                                            @ExampleObject(name = "successful delete",
                                                    value = "CartItems deleted successfully!"
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
                                                    name = "not admin or unauthorized",
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
                                                    name = "deleting another user's cartItems while having role user",
                                                    description = "trying to delete another user's cartItems using a JWT with role user, " +
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

    //TODO TEST THIS ENDPOINT
    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteAllByUserId (@PathVariable Integer userId){
        this.cartItemService.deleteAllByUserId(userId);
        return ResponseEntity.ok("CartItems of user: " + userId + " deleted successfully!");
    }

}
