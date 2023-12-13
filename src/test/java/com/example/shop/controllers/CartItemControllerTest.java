package com.example.shop.controllers;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.dtos.CartItemResponseDTO;
import com.example.shop.dtos.converters.CartItemResponseDTOToCartItemConverter;
import com.example.shop.dtos.converters.CartItemToCartItemResponseDTOConverter;
import com.example.shop.models.CartItem;
import com.example.shop.models.Item;
import com.example.shop.models.User;
import com.example.shop.services.CartItemService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = CartItemController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
//use embedded h2 database for tests not the postgress db in docker container
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
class CartItemControllerTest {
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
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

    @Autowired
    CartItemToCartItemResponseDTOConverter cartItemToCartItemResponseDTOConverter;
    @Autowired
    CartItemResponseDTOToCartItemConverter cartItemResponseDTOToCartItemConverter;

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
        u1.setCartItems(null);

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
        u2.setCartItems(null);


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

        cartItemsList.add(ci1);
        cartItemsList.add(ci2);
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
    void testFindCartItemByIdSuccess() throws Exception {
        //Given
        given(this.cartItemService.findById(id1)).willReturn(this.ci1);

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/3/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.userId").value(3))
                .andExpect(jsonPath("$.id.itemId").value(1))
                .andExpect(jsonPath("$.userDTO.id").value(u1.getId()))
                .andExpect(jsonPath("$.itemDTO.id").value(i1.getId()))
                .andExpect(jsonPath("$.quantity").value("5"));
    }

    @Test
    void testFindCartItemByIdNotFound() throws Exception {
        given(this.cartItemService.findById(Mockito.any(CartItemId.class))).willThrow(new ObjectNotFoundException("cartItem",id1));

        this.mockMvc.perform(get(this.baseUrl + "/cartItems/3/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find cartItem with id CartItemId(userId=3, itemId=1)"));
    }

    @Test
    void testFindAllCartItemSuccess() throws Exception {
        given(this.cartItemService.findAll()).willReturn(this.cartItemsList);

        this.mockMvc.perform(get(this.baseUrl + "/cartItems").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(this.cartItemsList.size())))
                .andExpect(jsonPath("$[0].id.userId").value(this.u1.getId()))
                .andExpect(jsonPath("$[1].id.userId").value(this.u2.getId()));
    }
    //
    @Test
    void testAddCartItemSuccess() throws Exception {
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(u1.getId(),i2.getId());

        ci3.setId(cid3);
        ci3.setItem(i2);
        ci3.setUser(u1);
        ci3.setQuantity(1);
        ci3.setTotalCost(new BigDecimal(10));

        //given
        CartItemResponseDTO cartItemResponseDTO = this.cartItemToCartItemResponseDTOConverter.convert(ci3);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemResponseDTO);



        given(this.cartItemService.save(Mockito.any(CartItem.class))).willReturn(ci3);

        this.mockMvc.perform(post(this.baseUrl + "/cartItems/" + cid3.getUserId() + "/" + cid3.getItemId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ci3.getId()))
                .andExpect(jsonPath("$.itemDTO.id").value(ci3.getItem().getId()))
                .andExpect(jsonPath("$.userDTO.id").value(ci3.getUser().getId()))
                .andExpect(jsonPath("$.quantity").value(ci3.getQuantity()))
                .andExpect(jsonPath("$.id.userId").value(ci3.getId().getUserId()))
                .andExpect(jsonPath("$.id.itemId").value(ci3.getId().getItemId()))
                .andExpect(jsonPath("$.totalCost").value(ci3.getTotalCost()));
    }
    //
    @Test
    void testAddCartItemBadRequest() throws Exception {
        //given
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(u1.getId(),i2.getId());

        ci3.setId(null);
        ci3.setItem(null);
        ci3.setUser(u1);
        ci3.setQuantity(1);
        ci3.setTotalCost(new BigDecimal(10));

        //given
        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO(null,null,null,-5,new BigDecimal(6));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemResponseDTO);


        given(this.cartItemService.save(Mockito.any(CartItem.class))).willReturn(ci3);

        this.mockMvc.perform(post(this.baseUrl + "/cartItems/32/62")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.quantity").value("must be greater than 0"))
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    //TODO add test for adding items with invalid item id , user id
    @Test
    void testUpdateCartItemSuccess() throws Exception {
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(u1.getId(),i2.getId());

        ci3.setId(cid3);
        ci3.setItem(i2);
        ci3.setUser(u1);
        ci3.setQuantity(1);
        ci3.setTotalCost(new BigDecimal(10));

        //given
        CartItemResponseDTO cartItemResponseDTO = this.cartItemToCartItemResponseDTOConverter.convert(ci3);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemResponseDTO);


        given(this.cartItemService.update(Mockito.any(CartItem.class))).willReturn(ci3);

        this.mockMvc.perform(put(this.baseUrl + "/cartItems/3/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ci3.getId()))
                .andExpect(jsonPath("$.itemDTO.id").value(ci3.getItem().getId()))
                .andExpect(jsonPath("$.userDTO.id").value(ci3.getUser().getId()))
                .andExpect(jsonPath("$.quantity").value(ci3.getQuantity()))
                .andExpect(jsonPath("$.id.userId").value(ci3.getId().getUserId()))
                .andExpect(jsonPath("$.id.itemId").value(ci3.getId().getItemId()))
                .andExpect(jsonPath("$.totalCost").value(ci3.getTotalCost()));
    }
    @Test
    void testUpdateCartItemNotFound () throws Exception{
        //given
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(u1.getId(),i2.getId());

        ci3.setId(cid3);
        ci3.setItem(i2);
        ci3.setUser(u1);
        ci3.setQuantity(1);
        ci3.setTotalCost(new BigDecimal(10));

        //given
        CartItemResponseDTO cartItemResponseDTO = this.cartItemToCartItemResponseDTOConverter.convert(ci3);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemResponseDTO);


        given(this.cartItemService.update(Mockito.any(CartItem.class))).willThrow(new ObjectNotFoundException("cartItem", ci3.getId()));

        this.mockMvc.perform(put(this.baseUrl + "/cartItems/3/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find cartItem with id CartItemId(userId=3, itemId=2)"));
    }

    @Test
    void testDeleteCartItemSuccess () throws Exception{
        doNothing().when(this.cartItemService).delete(ci1.getId());

        this.mockMvc.perform(delete(this.baseUrl + "/cartItems/3/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("CartItem deleted successfully!"));
    }

    @Test
    void testDeleteCartItemNotFound () throws Exception {
        doThrow(new ObjectNotFoundException("cartItem", ci1.getId())).when(this.cartItemService).delete(ci1.getId());

        this.mockMvc.perform(delete(this.baseUrl + "/cartItems/3/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find cartItem with id CartItemId(userId=3, itemId=1)"));
    }
}