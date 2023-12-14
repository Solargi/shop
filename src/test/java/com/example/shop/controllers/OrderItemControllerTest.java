package com.example.shop.controllers;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.dtos.ItemDTO;
import com.example.shop.dtos.OrderItemRequestDTO;
import com.example.shop.dtos.converters.OrderItemRequestDTOToOrderItemConverter;
import com.example.shop.models.*;
import com.example.shop.services.ItemService;
import com.example.shop.services.OrderItemService;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(value = OrderItemController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
//use embedded h2 database for tests not the postgress db in docker container
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
class OrderItemControllerTest {
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderItemRequestDTOToOrderItemConverter orderItemRequestDTOToOrderItemConverter;


    @MockBean
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
        o1.setShippingAddress(a1);

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
        orderItemList.add(oi2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindOrderItemByIdSuccess() throws Exception {
        //Given
        given(this.orderItemService.findById(oi1.getId())).willReturn(this.oi1);

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orderItems/1/1").accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(oi1.getId().getOrderId()))
                .andExpect(jsonPath("$.id.itemId").value(oi1.getId().getItemId()))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(oi1.getOrder().getShippingCost()))
                .andExpect(jsonPath("$.totalCost").value(oi1.getTotalCost()))
                .andExpect(jsonPath("$.quantity").value(oi1.getQuantity()));
    }

    @Test
    void testFindOrderItemByIdNotFound() throws Exception {
        given(this.orderItemService.findById(oi1.getId())).willThrow(new ObjectNotFoundException("orderItem",oi1.getId()));

        this.mockMvc.perform(get(this.baseUrl + "/orderItems/1/1").accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id " + oi1.getId()));
    }

    @Test
    void testFindAllOrderItemSuccess() throws Exception {
        given(this.orderItemService.findAll()).willReturn(this.orderItemList);

        this.mockMvc.perform(get(this.baseUrl + "/orderItems").accept(MediaType.APPLICATION_JSON))
                
                .andExpect(jsonPath("$", hasSize(this.orderItemList.size())))
                .andExpect(jsonPath("$[0].id").value(this.oi1.getId()))
                .andExpect(jsonPath("$[1].id").value(this.oi2.getId()));
    }

    @Test
    void testAddOrderItemSuccess() throws Exception {
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(oi1.getQuantity());
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        given(this.orderItemService.save(Mockito.any(OrderItem.class))).willReturn(oi1);

        this.mockMvc.perform(post(this.baseUrl + "/orderItems/" + oi1.getId().getOrderId() + "/" + oi1.getId().getItemId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(oi1.getId().getOrderId()))
                .andExpect(jsonPath("$.id.itemId").value(oi1.getId().getItemId()))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(oi1.getOrder().getShippingCost()))
                .andExpect(jsonPath("$.totalCost").value(oi1.getTotalCost()))
                .andExpect(jsonPath("$.quantity").value(oi1.getQuantity()));
    }

    @Test
    void testAddOrderItemBadRequest() throws Exception {
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(0);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        given(this.orderItemService.save(Mockito.any(OrderItem.class))).willReturn(oi1);

        this.mockMvc.perform(post(this.baseUrl + "/orderItems/41/23")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.quantity").value("must be greater than 0"));
    }
    @Test
    void testUpdateOrderItemSuccess() throws Exception {
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(1);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        given(this.orderItemService.update(Mockito.any(OrderItem.class))).willReturn(oi1);

        this.mockMvc.perform(put(this.baseUrl + "/orderItems/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(oi1.getId().getOrderId()))
                .andExpect(jsonPath("$.id.itemId").value(oi1.getId().getItemId()))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(oi1.getOrder().getShippingCost()))
                .andExpect(jsonPath("$.totalCost").value(oi1.getTotalCost()))
                .andExpect(jsonPath("$.quantity").value(oi1.getQuantity()));
    }
    @Test
    void testUpdateOrderItemNotFound () throws Exception{
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(1);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        given(this.orderItemService.update(Mockito.any(OrderItem.class))).willThrow(new ObjectNotFoundException("orderItem", oi1.getId()));

        this.mockMvc.perform(put(this.baseUrl + "/orderItems/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id " + oi1.getId()));
    }

    @Test
    void testDeleteOrderItemSuccess () throws Exception{
        doNothing().when(this.orderItemService).delete(oi1.getId());

        this.mockMvc.perform(delete(this.baseUrl + "/orderItems/1/1")
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(content().string("OrderItem deleted successfully!"));
    }

    @Test
    void testDeleteOrderItemNotFound () throws Exception {
        doThrow(new ObjectNotFoundException("orderItem", oi1.getId())).when(this.orderItemService).delete(oi1.getId());

        this.mockMvc.perform(delete(this.baseUrl + "/orderItems/1/1")
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id " + oi1.getId()));
    }
}