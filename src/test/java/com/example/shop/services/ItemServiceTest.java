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
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    //create mock of itemRepository (it simulates itemRepository without calling the real one)
    @Mock
    ItemRepository itemRepository;

    @InjectMocks //  inject mocked itemRepository in itemService
//    Instance of tested class
    ItemService itemService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdSuccess(){
        // Given. Arrange inputs and targets. Define the behavior of the Mock object ItemRepository
        //we can either conenct to a db or create fake data directly here:
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("a");
        item1.setCartItems(null);
        item1.setOrderItems(null);
        item1.setDescription("yay");
        item1.setPrice(new BigDecimal("32.1"));
        item1.setImageUrl("image");
        item1.setAvailableQuantity(new BigDecimal("2"));

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
}