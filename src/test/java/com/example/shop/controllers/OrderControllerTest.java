package com.example.shop.controllers;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.dtos.CartItemResponseDTO;
import com.example.shop.dtos.OrderDTO;
import com.example.shop.dtos.converters.OrderToOrderDTOConverter;
import com.example.shop.models.*;
import com.example.shop.services.OrderService;
import com.example.shop.system.exceptions.GenericException;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(value = OrderController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class OrderControllerTest {
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
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

    @Autowired
    OrderToOrderDTOConverter orderToOrderDTOConverter;


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
        o1.setShippingAddress(a1);

        o2.setUser(u2);
        o2.setId(1);
        o2.setTotalCost(new BigDecimal(3094));
        o2.setPaid(true);
        o2.setStatus("paid");
        o2.setShippingCost(new BigDecimal(10));
        o2.setOrderItemList(null);
        o2.setShippingAddress(a1);
        orderList.add(o1);
        orderList.add(o2);
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void testFindCartItemByIdSuccess() throws Exception {
        //Given
        given(this.orderService.findById(o1.getId())).willReturn(o1);

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orders/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(o1.getId()))
                .andExpect(jsonPath("$.totalCost").value(o1.getTotalCost()))
                .andExpect(jsonPath("$.user.id").value(u1.getId()))
                .andExpect(jsonPath("$.status").value(o1.getStatus()))
                .andExpect(jsonPath("$.shippingAddress.id").value(o1.getShippingAddress().getId()));
    }

    @Test
    void testFindCartItemByIdNotFound() throws Exception {
        given(this.orderService.findById(Mockito.any(Integer.class))).willThrow(new ObjectNotFoundException("order",o1.getId()));

        this.mockMvc.perform(get(this.baseUrl + "/orders/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find order with id " + o1.getId()));
    }

    @Test
    void testFindAllCartItemSuccess() throws Exception {
        given(this.orderService.findAll()).willReturn(this.orderList);

        this.mockMvc.perform(get(this.baseUrl + "/orders").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(this.orderList.size())))
                .andExpect(jsonPath("$[0].id").value(this.o1.getId()))
                .andExpect(jsonPath("$[1].id").value(this.o2.getId()));
    }
    //
    @Test
    void testAddOrderSuccess() throws Exception {
        Order order = new Order();
        order.setStatus("processing");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(10));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(u1.getAddresses().get(0));
        order.setUser(u1);
        order.setId(1);
//        u1.setOrderList(new ArrayList<>());

        for (CartItem cartItem : u1.getCartItems()){
            order.addOrderItem(new OrderItem(order, cartItem));
        }

        //given
        given(this.orderService.save(Mockito.any(Integer.class))).willReturn(order);

        this.mockMvc.perform(post(this.baseUrl + "/orders/" + u1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.totalCost").value(order.getTotalCost()))
                .andExpect(jsonPath("$.status").value(order.getStatus()))
                .andExpect(jsonPath("$.user.id").value(order.getUser().getId()))
                .andExpect(jsonPath("$.shippingCost").value(order.getShippingCost()))
                .andExpect(jsonPath("$.paid").value(order.getPaid()))
                .andExpect(jsonPath("$.shippingAddress.state").value(order.getShippingAddress().getState()));
    }
//    //
    @Test
    void testAddOrderBadRequest() throws Exception {
        Order order = new Order();
        order.setStatus(null);
        order.setPaid(true);
        order.setShippingCost(null);
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(u1.getAddresses().get(0));
        order.setUser(u1);
        order.setId(null);
        u1.setOrderList(new ArrayList<>());



        //given
        given(this.orderService.save(Mockito.any(Integer.class))).willThrow(new GenericException("User's cart has no items in it"));

        this.mockMvc.perform(post(this.baseUrl + "/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$").value("User's cart has no items in it"));
    }

    @Test
    void testUpdateOrderSuccess() throws Exception {
        Order order = new Order();
        order.setStatus("processing");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(10));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(u1.getAddresses().get(0));
        order.setUser(u1);
        order.setId(1);
//        u1.setOrderList(new ArrayList<>());

        for (CartItem cartItem : u1.getCartItems()){
            order.addOrderItem(new OrderItem(order, cartItem));
        }

        //given
        OrderDTO orderDTO = this.orderToOrderDTOConverter.convert(order);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderDTO);


        given(this.orderService.update(eq(o1.getId()),Mockito.any(Order.class))).willReturn(order);

        this.mockMvc.perform(put(this.baseUrl + "/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.status").value(order.getStatus()))
                .andExpect(jsonPath("$.shippingAddress.state").value(order.getShippingAddress().getState()))
                .andExpect(jsonPath("$.totalCost").value(order.getTotalCost()))
                .andExpect(jsonPath("$.user.id").value(order.getUser().getId()));
    }
    @Test
    void testUpdateOrderNotFound () throws Exception{
        //given
        Order order = new Order();
        order.setStatus("processing");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(10));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(u1.getAddresses().get(0));
        order.setUser(u1);
        order.setId(1);
//        u1.setOrderList(new ArrayList<>());

        for (CartItem cartItem : u1.getCartItems()){
            order.addOrderItem(new OrderItem(order, cartItem));
        }

        //given
        OrderDTO orderDTO = this.orderToOrderDTOConverter.convert(order);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderDTO);


        given(this.orderService.update(eq(order.getId()),Mockito.any(Order.class))).willThrow(new ObjectNotFoundException("order", order.getId()));

        this.mockMvc.perform(put(this.baseUrl + "/orders/"+ order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find order with id " + order.getId()));
    }

    @Test
    void testDeleteOrderSuccess () throws Exception{
        doNothing().when(this.orderService).delete(o1.getId());

        this.mockMvc.perform(delete(this.baseUrl + "/orders/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully!"));
    }

    @Test
    void testDeleteOrderNotFound () throws Exception {
        doThrow(new ObjectNotFoundException("order", o1.getId())).when(this.orderService).delete(o1.getId());

        this.mockMvc.perform(delete(this.baseUrl + "/orders/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find order with id " + o1.getId()));
    }
}
