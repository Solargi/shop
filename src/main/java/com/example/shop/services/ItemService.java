package com.example.shop.services;

import com.example.shop.models.CartItem;
import com.example.shop.models.Item;
import com.example.shop.repositories.ItemRepository;
import com.example.shop.system.exceptions.ObjectAlreadyExistException;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional //all methods in services are seen as transactions, if fails -> rolls back db
// @Transactional can be used to class lever for all methods or method lever for single methods
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item findById(Integer itemId){
        return this.itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("item",itemId));
    }

    public List<Item> findAll(){
        return this.itemRepository.findAll();
    }

    public Item save(Item item){
        // check if item already exist before saving it
        if (item.getId() != null){
            Optional<Item> foundCartItemOpt = this.itemRepository.findById(item.getId());
            if (foundCartItemOpt.isPresent()) {
                throw new ObjectAlreadyExistException("item", item.getId());
            }
        }
        return this.itemRepository.save(item);
    }

    public Item update(Integer itemId,  Item update){
        Item oldItem = this.itemRepository.findById(itemId).orElseThrow(()->new ObjectNotFoundException("item",itemId));
        oldItem.setName(update.getName());
        oldItem.setPrice(update.getPrice());
        oldItem.setDescription(update.getDescription());
        oldItem.setAvailableQuantity(update.getAvailableQuantity());
        oldItem.setImageUrl(update.getImageUrl());
        return this.itemRepository.save(oldItem);
    }

    public void delete(Integer itemId){
        Item item = this.itemRepository.findById(itemId).orElseThrow(()-> new ObjectNotFoundException("item",itemId));
        this.itemRepository.deleteById(itemId);
    }

}
