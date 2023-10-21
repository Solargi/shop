package com.example.shop.services;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.models.CartItem;
import com.example.shop.models.CartItem;
import com.example.shop.models.Item;
import com.example.shop.models.User;
import com.example.shop.repositories.CartItemRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {
    @Mock
    CartItemRepository cartItemRepository;
    
    @InjectMocks
    CartItemService cartItemService;

    User u1 = new User();
    User u2 = new User();
    Item i1 = new Item();
    Item i2 = new Item();

    CartItem ci1 = new CartItem();
    CartItem ci2 = new CartItem();
    CartItemId id1 = new CartItemId();
    CartItemId id3 = new CartItemId();
    CartItemId id2 = new CartItemId();

    List<CartItem> cartItemsList = new ArrayList<CartItem>();

    @BeforeEach
    void setUp() {
        i1.setId(1);
        i1.setName("a");
        //TODO: ASSIGN TO ACTUAL OBJECTS
        i1.setCartItems(null);
        i1.setOrderItems(null);
        i1.setDescription("yay");
        i1.setPrice(new BigDecimal("32.1"));
        i1.setImageUrl("image");
        i1.setAvailableQuantity(new BigDecimal("2"));


        i2.setId(2);
        i2.setName("b");
        //TODO: ASSIGN TO ACTUAL OBJECTS
        i2.setCartItems(null);
        i2.setOrderItems(null);
        i2.setDescription("yay2");
        i2.setPrice(new BigDecimal("32.1"));
        i2.setImageUrl("image");
        i2.setAvailableQuantity(new BigDecimal("2"));

        u1.setId(3);
        u1.setName("a");
        u1.setSurname("b");
        u1.setUsername("u1");
        u1.setPassword("q");
        u1.setEmail("c");
        u1.setRoles("admin");
        u1.setBirthDate("yay");
        //TODO: ASSIGN THEM TO ACTUAL OBJECTS
        u1.setAddresses(null);
        u1.setOrderList(null);
        u1.setCartItem(null);

        u2.setId(4);
        u2.setName("d");
        u2.setSurname("e");
        u2.setUsername("u2");
        u2.setPassword("f");
        u2.setEmail("g");
        u2.setRoles("user");
        u2.setBirthDate("yay2");
        //TODO: ASSIGN THEM TO ACTUAL OBJECTS
        u2.setAddresses(null);
        u2.setOrderList(null);
        u2.setCartItem(null);

        id1.setItemId(i1.getId());
        id1.setUserId(u1.getId());
        id3.setUserId(u2.getId());
        id3.setItemId(i2.getId());
        id2.setItemId(i2.getId());
        id2.setItemId(u1.getId());


        ci1.setId(id1);
        ci1.setItem(i1);
        ci1.setUser(u1);
        ci1.setQuantity(5);

        ci2.setId(id3);
        ci2.setUser(u2);
        ci2.setItem(i2);
        ci2.setQuantity(2);

        List<CartItem> ciu1 = new ArrayList<>();
        ciu1.add(ci1);
        List<CartItem> ciu2 = new ArrayList<>();
        ciu2.add(ci2);
        ciu2.add(ci2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdSuccess(){
        //define behaviour of mock object
        given(cartItemRepository.findById(ci1.getId())).willReturn(Optional.of(ci1));

        // When. Act on target behavior. when steps should cover the method to be tested
        CartItem returnedCartItem = cartItemService.findById(ci1.getId());
        
        // Then. Assert expected outcomes.
        assertThat(returnedCartItem.getId()).isEqualTo(ci1.getId());
        assertThat(returnedCartItem.getUser()).isEqualTo(ci1.getUser());
        assertThat(returnedCartItem.getItem()).isEqualTo(ci1.getItem());
        assertThat(returnedCartItem.getQuantity()).isEqualTo(ci1.getQuantity());
    }

    @Test
    void findByIdNotFound(){
        //Given
        given(cartItemRepository.findById(Mockito.any(CartItemId.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            CartItem returnedCartItem = cartItemService.findById(ci1.getId());});

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("could not find cartitem with id CartItemId(userId=3, itemId=1)");
    }

    @Test
    void testFindAllSuccess(){
//        given
        given(cartItemRepository.findAll()).willReturn(this.cartItemsList);
//        when
        List<CartItem> foundAddresss = cartItemService.findAll();
//        then
        assertThat(foundAddresss.size()).isEqualTo(this.cartItemsList.size());
        verify(cartItemRepository, times(1)).findAll();

    }

    @Test
    void testSaveSuccess(){
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(u1.getId(),i2.getId());

        ci3.setId(cid3);
        ci3.setItem(i2);
        ci3.setUser(u1);
        ci3.setQuantity(1);

        given(cartItemRepository.save(ci3)).willReturn(ci3);

        CartItem savedCartItem = cartItemService.save(ci3);
        assertThat(savedCartItem.getId()).isEqualTo(ci3.getId());
        assertThat(savedCartItem.getId()).isEqualTo(ci3.getId());
        assertThat(savedCartItem.getUser()).isEqualTo(ci3.getUser());
        assertThat(savedCartItem.getItem()).isEqualTo(ci3.getItem());
        assertThat(savedCartItem.getQuantity()).isEqualTo(ci3.getQuantity());     
    }

    @Test
    void testUpdateSuccess(){
       CartItem ci3 = new CartItem();
       CartItemId cid3 = new CartItemId(u1.getId(),i2.getId());
       ci3.setId(cid3);
       ci3.setItem(i2);
       ci3.setUser(u1);
       ci3.setQuantity(1);

        given(cartItemRepository.findById(ci1.getId())).willReturn(Optional.of(ci1));
        //when savind address 1 already has the new values
        given(cartItemRepository.save(ci1)).willReturn(ci1);

        CartItem updated1 = cartItemService.update(ci1.getId(),ci3);
        assertThat(updated1.getId()).isEqualTo(ci3.getId());
        assertThat(updated1.getQuantity()).isEqualTo(ci3.getQuantity());
        verify(cartItemRepository, times(1)).findById(id1);
        verify(cartItemRepository, times(1)).save(ci1);

    }

    @Test
    void testUpdateNotFound(){
        given(cartItemRepository.findById(id2)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            cartItemService.update(id2,ci1);
        });

        verify(cartItemRepository,times(1)).findById(id2);

    }
    @Test
    void testDeleteSuccess(){
        given(cartItemRepository.findById(id1)).willReturn(Optional.of(ci1));
        doNothing().when(cartItemRepository).deleteById(id1);

        cartItemService.delete(id1);

        verify(cartItemRepository, times(1)).deleteById(id1);


    }

    @Test
    void testDeleteNotFound(){
        given(cartItemRepository.findById(id2)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            cartItemService.delete(id2);
        });

        verify(cartItemRepository, times(1)).findById(id2);


    }


}