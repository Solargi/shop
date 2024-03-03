package com.example.shop.services;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.models.*;
import com.example.shop.repositories.ItemRepository;
import com.example.shop.repositories.OrderItemRepository;
import com.example.shop.repositories.OrderRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import org.aspectj.weaver.ast.Or;
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
class OrderItemServiceTest {
    @Mock
    OrderItemRepository orderItemRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    OrderItemService orderItemService;

    OrderItem oi1 = new OrderItem();
    OrderItem oi2 = new OrderItem();

    List<OrderItem> orderItemList = new ArrayList<>();

    User u1 = new User();
    User u2 = new User();
    Item i1 = new Item();
    Item i2 = new Item();
    Address a1 = new Address();
    List<Address> addressesList = new ArrayList<Address>();

    CartItem ci1 = new CartItem();
    CartItem ci2 = new CartItem();
    CartItemId id1 = new CartItemId();
    CartItemId id3 = new CartItemId();
    CartItemId id2 = new CartItemId();

    List<CartItem> cartItemsList = new ArrayList<CartItem>();
    Order o1 = new Order();
    Order o2 = new Order();
    List<Order> orderList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1.setId(1);
        i1.setName("a");
        //TODO: ASSIGN TO ACTUAL OBJECTS
        i1.setCartItems(new ArrayList<CartItem>());
        i1.setOrderItems(null);
        i1.setDescription("yay");
        i1.setPrice(new BigDecimal("32.1"));
        i1.setImageUrl("image");
        i1.setAvailableQuantity(new BigDecimal("2"));


        i2.setId(2);
        i2.setName("b");
        //TODO: ASSIGN TO ACTUAL OBJECTS
        i2.setCartItems(new ArrayList<CartItem>());
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
        u1.setCartItems(new ArrayList<CartItem>());

        a1.setId(1);
        a1.setCountry("a");
        a1.setCity("b");
        a1.setStreet("c");
        a1.setState("d");
        a1.setZipCode(56);
        a1.setUser(u1);

        addressesList.add(a1);
        u1.setAddresses(addressesList);

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
        u2.setCartItems(new ArrayList<CartItem>());

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
        ci1.setTotalCost(new BigDecimal(20));

        ci2.setId(id3);
        ci2.setUser(u2);
        ci2.setItem(i2);
        ci2.setQuantity(2);
        ci2.setTotalCost(new BigDecimal(30));

        List<CartItem> ciu1 = new ArrayList<>();
        ciu1.add(ci1);
        List<CartItem> ciu2 = new ArrayList<>();
        ciu2.add(ci2);
        ciu2.add(ci2);
        u1.setCartItems(ciu1);


        o1.setUser(u1);
        o1.setId(1);
        o1.setTotalCost(new BigDecimal(3094));
        o1.setPaid(true);
        o1.setStatus("pending");
        o1.setShippingCost(new BigDecimal(10));
        o1.setOrderItemList(new ArrayList<>());

        o2.setUser(u2);
        o2.setId(1);
        o2.setTotalCost(new BigDecimal(3094));
        o2.setPaid(true);
        o2.setStatus("paid");
        o2.setShippingCost(new BigDecimal(10));
        o2.setOrderItemList(new ArrayList<>());

        oi1.setId(new OrderItemId(o1.getId(), i1.getId()));
        oi1.setOrder(o1);
        oi1.setTotalCost(new BigDecimal(10));
        oi1.setItem(i1);
        oi1.setQuantity(1);

        oi2.setId(new OrderItemId(o1.getId(),i2.getId()));
        oi2.setOrder(o1);
        oi2.setQuantity(2);
        oi2.setItem(i2);
        oi2.setTotalCost(new BigDecimal(10));

        orderItemList.add(oi1);
        orderItemList.add(oi1);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findByIdSuccess(){
        // Given. Arrange inputs and targets. Define the behavior of the Mock object ItemRepository
        //we can either conenct to a db or create fake data directly here (or in setup)

        //define behaviour of mock object
        given(orderItemRepository.findById(oi1.getId())).willReturn(Optional.of(oi1));

        // When. Act on target behavior. when steps should cover the method to be tested
        OrderItem returnedOrderItem = orderItemService.findById(oi1.getId());


        // Then. Assert expected outcomes.
        assertThat(returnedOrderItem.getId()).isEqualTo(oi1.getId());
        assertThat(returnedOrderItem.getItem()).isEqualTo(oi1.getItem());
        assertThat(returnedOrderItem.getQuantity()).isEqualTo(oi1.getQuantity());
        assertThat(returnedOrderItem.getTotalCost()).isEqualTo(oi1.getTotalCost());
        assertThat(returnedOrderItem.getOrder()).isEqualTo(oi1.getOrder());
    }

    @Test
    void findByIdNotFound(){
        //Given
        given(orderItemRepository.findById(Mockito.any(OrderItemId.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            OrderItem returnedOrderItem = orderItemService.findById(oi1.getId());});

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("could not find orderItem with id " + oi1.getId());
    }

    @Test
    void testFindAllSuccess(){
//        given
        given(orderItemRepository.findAll()).willReturn(this.orderItemList);
//        when
        List<OrderItem> foundItems = orderItemService.findAll();
//        then
        assertThat(foundItems.size()).isEqualTo(this.orderItemList.size());
        verify(orderItemRepository, times(1)).findAll();

    }

    @Test
    void testSaveSuccess(){

        given(itemRepository.findById(i1.getId())).willReturn(Optional.of(i1));
        given(orderRepository.findById(o1.getId())).willReturn(Optional.of(o1));
        given(orderItemRepository.save(oi1)).willReturn(oi1);

        OrderItem savedOrderItem = orderItemService.save(oi1);
        assertThat(savedOrderItem.getId()).isEqualTo(oi1.getId());
        assertThat(savedOrderItem.getItem()).isEqualTo(oi1.getItem());
        assertThat(savedOrderItem.getQuantity()).isEqualTo(oi1.getQuantity());
        assertThat(savedOrderItem.getTotalCost()).isEqualTo(oi1.getTotalCost());
        assertThat(savedOrderItem.getOrder()).isEqualTo(oi1.getOrder());
    }

    @Test
    void testUpdateSuccess(){
       OrderItem oi3 = new OrderItem();
       oi3.setId(oi1.getId());
       oi3.setQuantity(2);

        given(orderItemRepository.findById(oi1.getId())).willReturn(Optional.of(oi1));
        given(itemRepository.findById(i1.getId())).willReturn(Optional.of(i1));
        //when savind item 1 already has the new values
        given(orderItemRepository.save(oi1)).willReturn(oi1);
        given(orderRepository.findById(oi3.getId().getOrderId())).willReturn(Optional.of(o1));



        OrderItem updated1 = orderItemService.update(oi3);

        assertThat(updated1.getId()).isEqualTo(oi1.getId());
        assertThat(updated1.getQuantity()).isEqualTo(oi3.getQuantity());
        verify(orderItemRepository, times(1)).findById(oi1.getId());
        verify(orderItemRepository, times(1)).save(oi1);

    }

    @Test
    void testUpdateNotFound(){
        given(orderItemRepository.findById(oi1.getId())).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            orderItemService.update(oi1);
        });

        verify(orderItemRepository,times(1)).findById(oi1.getId());

    }
    @Test
    void testDeleteSuccess(){
        given(orderItemRepository.findById(oi1.getId())).willReturn(Optional.of(oi1));
        given(itemRepository.findById(i1.getId())).willReturn(Optional.of(i1));
        given(orderRepository.findById(oi1.getId().getOrderId())).willReturn(Optional.of(o1));
        doNothing().when(orderItemRepository).deleteById(oi1.getId());
        orderItemService.delete(oi1.getId());

        verify(orderItemRepository, times(1)).deleteById(oi1.getId());


    }
//
    @Test
    void testDeleteNotFound(){
        given(orderItemRepository.findById(oi1.getId())).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            orderItemService.delete(oi1.getId());
        });

        verify(orderItemRepository, times(1)).findById(oi1.getId());
    }

}