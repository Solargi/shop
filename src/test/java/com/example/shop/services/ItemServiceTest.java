package com.example.shop.services;

import com.example.shop.models.Item;
import com.example.shop.repositories.ItemRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {


    //create mock of itemRepository (it simulates itemRepository without calling the real one)
    @Mock
    ItemRepository itemRepository;

    @InjectMocks //  inject mocked itemRepository in itemService
//    Instance of tested class
    ItemService itemService;

//  test entities
    Item item1 = new Item();
    Item item2 = new Item();
    List<Item> itemList = new ArrayList<Item>();


    @BeforeEach
    void setUp() {

        item1.setId(1);
        item1.setName("a");
        //TODO: ASSIGN TO ACTUAL OBJECTS
        item1.setCartItems(null);
        item1.setOrderItems(null);
        item1.setDescription("yay");
        item1.setPrice(new BigDecimal("32.1"));
        item1.setImageUrl("image");
        item1.setAvailableQuantity(new BigDecimal("2"));


        item2.setId(2);
        item2.setName("b");
        //TODO: ASSIGN TO ACTUAL OBJECTS
        item2.setCartItems(null);
        item2.setOrderItems(null);
        item2.setDescription("yay2");
        item2.setPrice(new BigDecimal("32.1"));
        item2.setImageUrl("image");
        item2.setAvailableQuantity(new BigDecimal("2"));


        itemList.add(item1);
        itemList.add(item2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdSuccess(){
        // Given. Arrange inputs and targets. Define the behavior of the Mock object ItemRepository
        //we can either conenct to a db or create fake data directly here (or in setup)

        //define behaviour of mock object
        given(itemRepository.findById(1)).willReturn(Optional.of(item1));

        // When. Act on target behavior. when steps should cover the method to be tested
        Item returnedItem = itemService.findById(1);


        // Then. Assert expected outcomes.
        assertThat(returnedItem.getId()).isEqualTo(item1.getId());
        assertThat(returnedItem.getCartItems()).isNull();
        assertThat(returnedItem.getPrice()).isEqualTo(item1.getPrice());
        assertThat(returnedItem.getDescription()).isEqualTo(item1.getDescription());
        assertThat(returnedItem.getName()).isEqualTo(item1.getName());
        assertThat(returnedItem.getImageUrl()).isEqualTo(item1.getImageUrl());
        assertThat(returnedItem.getOrderItems()).isNull();
        assertThat(returnedItem.getAvailableQuantity()).isEqualTo(item1.getAvailableQuantity());
    }

    @Test
    void findByIdNotFound(){
        //Given
        given(itemRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            Item returnedItem = itemService.findById(14);});

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("could not find item with id 14");
    }

    @Test
    void testFindAllSuccess(){
//        given
        given(itemRepository.findAll()).willReturn(this.itemList);
//        when
        List<Item> foundItems = itemService.findAll();
//        then
        assertThat(foundItems.size()).isEqualTo(this.itemList.size());
        verify(itemRepository, times(1)).findAll();

    }

    @Test
    void testSaveSuccess(){
        Item item3 = new Item();
        item3.setId(3);
        item3.setName("a");
        item3.setCartItems(null);
        item3.setOrderItems(null);
        item3.setDescription("yay");
        item3.setPrice(new BigDecimal("32.1"));
        item3.setImageUrl("image");
        item3.setAvailableQuantity(new BigDecimal("2"));

        given(itemRepository.save(item3)).willReturn(item3);

        Item savedItem = itemService.save(item3);
        assertThat(savedItem.getId()).isEqualTo(item3.getId());
        assertThat(savedItem.getCartItems()).isNull();
        assertThat(savedItem.getPrice()).isEqualTo(item3.getPrice());
        assertThat(savedItem.getDescription()).isEqualTo(item3.getDescription());
        assertThat(savedItem.getName()).isEqualTo(item3.getName());
        assertThat(savedItem.getImageUrl()).isEqualTo(item3.getImageUrl());
        assertThat(savedItem.getOrderItems()).isNull();
        assertThat(savedItem.getAvailableQuantity()).isEqualTo(item3.getAvailableQuantity());
    }

    @Test
    void testUpdateSuccess(){
        Item item3 = new Item();
        item3.setId(1);
        item3.setName("a");
        item3.setCartItems(null);
        item3.setOrderItems(null);
        item3.setDescription("3");
        item3.setPrice(new BigDecimal("32.1"));
        item3.setImageUrl("image");
        item3.setAvailableQuantity(new BigDecimal("2"));
        given(itemRepository.findById(1)).willReturn(Optional.of(item1));
        //when savind item 1 already has the new values
        given(itemRepository.save(item1)).willReturn(item1);



        Item updated1 = itemService.update(item3.getId(),item3);

        assertThat(updated1.getId()).isEqualTo(item1.getId());
        assertThat(updated1.getDescription()).isEqualTo(item3.getDescription());
        verify(itemRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).save(item1);

    }

    @Test
    void testUpdateNotFound(){
        given(itemRepository.findById(3232)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            itemService.update(3232,item1);
        });

        verify(itemRepository,times(1)).findById(3232);

    }
    @Test
    void testDeleteSuccess(){
        given(itemRepository.findById(1)).willReturn(Optional.of(item1));
        doNothing().when(itemRepository).deleteById(1);

        itemService.delete(1);

        verify(itemRepository, times(1)).deleteById(1);


    }

    @Test
    void testDeleteNotFound(){
        given(itemRepository.findById(4)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            itemService.delete(4);
        });

        verify(itemRepository, times(1)).findById(4);


    }
}