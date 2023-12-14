package com.example.shop.controllers;

import com.example.shop.dtos.ItemDTO;
import com.example.shop.dtos.converters.ItemDTOToItemConverter;
import com.example.shop.dtos.converters.ItemToItemDTOConverter;
import com.example.shop.models.Item;
import com.example.shop.services.ItemService;
import com.example.shop.services.UserService;
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


    @GetMapping("")
    public ResponseEntity<List<ItemDTO>> getItems(){
        List<Item> foundItems = this.itemService.findAll();
        //convert to dtos
        List<ItemDTO> foundItemsDTO = foundItems.stream()
                .map(this.itemToItemDTOConverter::convert)
                .toList();
        return ResponseEntity.ok(foundItemsDTO);

    }
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDTO> findItemById(@PathVariable("itemId") int itemId){
        Item foundItem = this.itemService.findById(itemId);
        return ResponseEntity.ok(this.itemToItemDTOConverter.convert(foundItem));
    }

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

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Integer itemId, @Valid @RequestBody ItemDTO itemDTO){
        Item item = this.itemDTOToItemConverter.convert(itemDTO);
        Item updatedItem = this.itemService.update(itemId,item);
        ItemDTO updatedItemDTO = this.itemToItemDTOConverter.convert(updatedItem);
        return ResponseEntity.ok(updatedItemDTO);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem (@PathVariable Integer itemId){
        this.itemService.delete(itemId);
        return ResponseEntity.ok("Item deleted successfully!");

    }



}
