package com.example.shop.services;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.models.*;
import com.example.shop.repositories.*;
import com.example.shop.system.exceptions.GenericException;
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
class OrderServiceTest {
    @Mock
    CartItemRepository cartItemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    @Mock
    OrderItemRepository orderItemRepository;
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

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
        o1.setOrderItemList(null);

        o2.setUser(u2);
        o2.setId(1);
        o2.setTotalCost(new BigDecimal(3094));
        o2.setPaid(true);
        o2.setStatus("paid");
        o2.setShippingCost(new BigDecimal(10));
        o2.setOrderItemList(null);
        orderList.add(o1);
        orderList.add(o2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdSuccess(){
        //define behaviour of mock object
        given(orderRepository.findById(o1.getId())).willReturn(Optional.of(o1));

        // When. Act on target behavior. when steps should cover the method to be tested
        Order returnedOrder = orderService.findById(o1.getId());

        // Then. Assert expected outcomes.
        assertThat(returnedOrder.getId()).isEqualTo(o1.getId());
        assertThat(returnedOrder.getUser()).isEqualTo(o1.getUser());
        assertThat(returnedOrder.getStatus()).isEqualTo(o1.getStatus());
        assertThat(returnedOrder.getTotalCost()).isEqualTo(o1.getTotalCost());
    }

    @Test
    void findByIdNotFound(){
        //Given
        given(orderRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            Order returnedOrder = orderService.findById(o1.getId());});

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("could not find order with id " + o1.getId());
    }

    @Test
    void testFindAllSuccess(){
//        given
        given(orderRepository.findAll()).willReturn(this.orderList);
//        when
        List<Order> foundOrders = orderService.findAll();
//        then
        assertThat(foundOrders.size()).isEqualTo(this.orderList.size());
        verify(orderRepository, times(1)).findAll();

    }

    @Test
    void testSaveSuccess(){
        Order order = new Order();
        order.setStatus("processing");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(10));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(u1.getAddresses().get(0));
        order.setUser(u1);
//        u1.setOrderList(new ArrayList<>());

        for (CartItem cartItem : u1.getCartItems()){
            order.addOrderItem(new OrderItem(order, cartItem));
        }
        //using any class because order is created inside the save method
        // so not possible to mock otherwise
        given(orderRepository.save(Mockito.any(Order.class))).willReturn(order);
        given(userRepository.findById(u1.getId())).willReturn(Optional.of(u1));

        Order savedOrder = orderService.save(u1.getId());
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getShippingCost()).isEqualTo(order.getShippingCost());
        assertThat(savedOrder.getUser()).isEqualTo(order.getUser());
        assertThat(savedOrder.getOrderItemList()).isEqualTo(order.getOrderItemList());
        assertThat(savedOrder.getTotalCost()).isEqualTo(order.getTotalCost());
    }
    @Test
    void testSaveFailUserHasNoCartItems(){
        Order order = new Order();
        order.setStatus("processing");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(10));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(u1.getAddresses().get(0));
        order.setUser(u2);
        given(userRepository.findById(u2.getId())).willReturn(Optional.of(u2));

        assertThrows(GenericException.class, ()->{
            orderService.save(u2.getId());
        });

        verify(userRepository,times(1)).findById(u2.getId());
        verify(orderRepository,times(0)).save(Mockito.any(Order.class));

    }

    @Test
    void testSaveFailUserNotFound(){
        Order order = new Order();
        order.setStatus("processing");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(10));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(u1.getAddresses().get(0));
        order.setUser(u2);
        given(userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            orderService.save(4325);
        });

        verify(userRepository,times(1)).findById(4325);
        verify(orderRepository,times(0)).save(Mockito.any(Order.class));

    }
//
    @Test
    void testUpdateSuccess(){
        Order order = new Order();
        order.setStatus("processing");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(10));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(u1.getAddresses().get(0));
        order.setUser(u1);

        given(orderRepository.findById(o1.getId())).willReturn(Optional.of(o1));
//        given(userRepository.findById(u1.getId())).willReturn(Optional.of(u1));
//        given(itemRepository.findById(i2.getId())).willReturn(Optional.of(i2));
        //when savind address 1 already has the new values
        given(orderRepository.save(o1)).willReturn(o1);

        Order updated1 = orderService.update(o1.getId(),order);
        // check that it doesn't allow to modify id
        assertThat(updated1.getId()).isEqualTo(o1.getId());
        assertThat(updated1.getOrderItemList()).isEqualTo(order.getOrderItemList());
        assertThat(updated1.getShippingCost()).isEqualTo(order.getShippingCost());
        assertThat(updated1.getShippingCost()).isEqualTo(o1.getShippingCost());
        verify(orderRepository, times(1)).findById(o1.getId());
        verify(orderRepository, times(1)).save(o1);

    }
//
    @Test
    void testUpdateNotFound(){
        given(orderRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            orderService.update(4,o1);
        });

        verify(orderRepository,times(1)).findById(4);

    }
    @Test
    void testDeleteSuccess(){
        given(orderRepository.findById(o1.getId())).willReturn(Optional.of(o1));
        doNothing().when(orderRepository).deleteById(o1.getId());

        orderService.delete(o1.getId());

        verify(orderRepository, times(1)).deleteById(o1.getId());
    }
//
    @Test
    void testDeleteNotFound(){
        given(orderRepository.findById(43)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            orderService.delete(43);
        });

        verify(orderRepository, times(1)).findById(43);


    }


}