package com.example.shop.controllers;

import com.example.shop.dtos.ItemDTO;
import com.example.shop.dtos.UserDTO;
import com.example.shop.dtos.converters.ItemDTOToItemConverter;
import com.example.shop.dtos.converters.ItemToItemDTOConverter;
import com.example.shop.models.Item;
import com.example.shop.models.User;
import com.example.shop.services.ItemService;
import com.example.shop.services.UserService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemToItemDTOConverter itemToItemDTOConverter;
    private final ItemDTOToItemConverter itemDTOToItemConverter;
    @Parameters({
            @Parameter(name = "ItemId", description = "The id of the target item", example = "1")
    })
    @Operation(
            summary = "get target item data",
            description = "get the target item's data",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ItemDTO.class),
                                    examples = {
                                            @ExampleObject(name = "Item List Example",
                                                    value =
                                                            "{" +
                                                            "        \"id\": 1," +
                                                            "        \"name\": \"i1\"," +
                                                            "        \"description\": \"something\"," +
                                                            "        \"price\": \"13\"," +
                                                            "        \"availableQuantity\": \"4\"" +
                                                            "}"
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
                                                    name = "item not found in database",
                                                    value = "Could not find item with id 1",
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
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDTO> findItemById(@PathVariable("itemId") int itemId){
        Item foundItem = this.itemService.findById(itemId);
        return ResponseEntity.ok(this.itemToItemDTOConverter.convert(foundItem));
    }

    @Operation(
            summary = "get all items data",
            description = "get an array containing all item's data (or an empty one if there is no item)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ItemDTO.class),
                                    examples = {
                                            @ExampleObject(name = "User List Example",
                                                    value = "[\n" +
                                                            "    {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"name\": \"i1\",\n" +
                                                            "        \"description\": \"something\",\n" +
                                                            "        \"price\": \"13\",\n" +
                                                            "        \"availableQuantity\": \"4\"\n" +
                                                            "    },\n" +
                                                            "    {\n" +
                                                            "        \"id\": 2,\n" +
                                                            "        \"name\": \"i2\",\n" +
                                                            "        \"description\": \"something2\",\n" +
                                                            "        \"price\": \"345\",\n" +
                                                            "        \"availableQuantity\": \"7\"\n" +
                                                            "    }\n" +
                                                            "]",
                                                    description = "array containing user's data"
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
    public ResponseEntity<List<ItemDTO>> getItems(){
        List<Item> foundItems = this.itemService.findAll();
        //convert to dtos
        List<ItemDTO> foundItemsDTO = foundItems.stream()
                .map(this.itemToItemDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundItemsDTO);

    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Create a new Item",
            description = "creates a new Item, only logged in account with role admin can create an item",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemDTO.class),
                            examples = {
                                    @ExampleObject(name = "Item Example",
                                            value =
                                                    "{" +
                                                            "\"name\": \"i1\"," +
                                                            "\"description\": \"something\"," +
                                                            "\"price\": \"13\"," +
                                                            "\"availableQuantity\": \"4\"" +
                                                            "}"
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
                                    schema = @Schema(implementation = ItemDTO.class),
                                    examples = {
                                            @ExampleObject(name = "Item Example",
                                                    value =
                                                            "{" +
                                                                    "        \"id\": 1," +
                                                                    "        \"name\": \"i1\"," +
                                                                    "        \"description\": \"something\"," +
                                                                    "        \"price\": \"13\"," +
                                                                    "        \"availableQuantity\": \"4\"" +
                                                                    "}"
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
                                                    value = "{" +
                                                            "    \"name\": \"should not be empty/null\"," +
                                                            "    \"price\": \"should not be empty/null\"," +
                                                            "    \"availableQuantity\": \"should not be empty/null\"" +
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
                                                    name = "posting an item while having role user",
                                                    description = "trying to post an item using a JWT with role user, " +
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
    @PostMapping("")
    //valid checks for validity of fields defined in ItemDto class with annotation
    // request body takes
    public ResponseEntity<ItemDTO> addItem(@Valid @RequestBody ItemDTO itemDTO){
        //convert dto to object
        Item item = this.itemDTOToItemConverter.convert(itemDTO);
        //save item
        Item savedItem = this.itemService.save(item);
        // reconvert to dto to get generated field id
        ItemDTO savedItemDTO = this.itemToItemDTOConverter.convert(savedItem);
        return ResponseEntity.ok(savedItemDTO);

    }


    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "ItemId", description = "The id of the target item", example = "1")
    })
    @Operation(
            summary = "update an Item",
            description = "updates Item, only logged in account with role admin can update an item",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemDTO.class),
                            examples = {
                                    @ExampleObject(name = "item example",
                                            value = "{ \"name\": \"i1\", " +
                                                    "\"description\": \"something\", " +
                                                    "\"price\": \"12\", " +
                                                    "\"imageUrl\": \"url2\", " +
                                                    "\"availableQuantity\": \"33\" }"
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
                                    schema = @Schema(implementation = ItemDTO.class),
                                    examples = {
                                            @ExampleObject(name = "item example",
                                                    value = "{ \"name\": \"i1\", " +
                                                            "\"description\": \"something\", " +
                                                            "\"price\": \"12\", " +
                                                            "\"imageUrl\": \"url2\", " +
                                                            "\"availableQuantity\": \"33\" }"
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
                                                    value = "{" +
                                                            "    \"name\": \"should not be empty/null\"," +
                                                            "    \"price\": \"should not be empty/null\"," +
                                                            "    \"availableQuantity\": \"should not be empty/null\"," +
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
                                                    name = "updating an item while having role user",
                                                    description = "trying to update an item using a JWT with role user, " +
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
    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Integer itemId, @Valid @RequestBody ItemDTO itemDTO){
        Item item = this.itemDTOToItemConverter.convert(itemDTO);
        Item updatedItem = this.itemService.update(itemId,item);
        ItemDTO updatedItemDTO = this.itemToItemDTOConverter.convert(updatedItem);
        return ResponseEntity.ok(updatedItemDTO);
    }



    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "ItemId", description = "The id of the target item", example = "1")
    })
    @Operation(
            summary = "delete an Item",
            description = "deletes Item, only logged in account with role admin can delete an item",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ItemDTO.class),
                                    examples = {
                                            @ExampleObject(name = "successful delete",
                                                    value = "Item deleted successfully!"
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
                                                    value = "{\"name\": \"should not be empty/null\", " +
                                                            "\"price\": \"should not be empty/null\", " +
                                                            "\"availableQuantity\": \"should not be empty/null\" }",
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
                                                    name = "deleting an item while having role user",
                                                    description = "trying to delete an item using a JWT with role user, " +
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
                                                    name = "item not found in database",
                                                    value = "Could not find item with id 1",
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
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem (@PathVariable Integer itemId){
        this.itemService.delete(itemId);
        return ResponseEntity.ok("Item deleted successfully!");

    }



}
