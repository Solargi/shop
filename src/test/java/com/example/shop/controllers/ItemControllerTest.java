package com.example.shop.controllers;

import com.example.shop.dtos.ItemDTO;
import com.example.shop.models.Item;
import com.example.shop.services.ItemService;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ItemController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
//use embedded h2 database for tests not the postgress db in docker container
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
class ItemControllerTest {
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemService itemService;


    Item item1 = new Item();
    Item item2 = new Item();
    List<Item> itemList = new ArrayList<Item>();


    @BeforeEach
    void setUp() {
        item1.setId(1);
        item1.setName("a");
        item1.setCartItems(null);
        item1.setOrderItems(null);
        item1.setDescription("yay");
        item1.setPrice(new BigDecimal("32.1"));
        item1.setImageUrl("image");
        item1.setAvailableQuantity(new BigDecimal("2"));


        item2.setId(2);
        item2.setName("b");
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
    void testFindItemByIdSuccess() throws Exception {
        //Given
        given(this.itemService.findById(1)).willReturn(this.item1);

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/items/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderItem").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.name").value("a"))
                .andExpect(jsonPath("$.description").value("yay"))
                .andExpect(jsonPath("$.price").value(32.1))
                .andExpect(jsonPath("$.imageUrl").value("image"))
                .andExpect(jsonPath("$.availableQuantity").value(2));
    }

    @Test
    void testFindItemByIdNotFound() throws Exception {
        given(this.itemService.findById(123)).willThrow(new ObjectNotFoundException("item",123));

        this.mockMvc.perform(get(this.baseUrl + "/items/123").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find item with id 123"));
    }

    @Test
    void testFindAllItemSuccess() throws Exception {
        given(this.itemService.findAll()).willReturn(this.itemList);

        this.mockMvc.perform(get(this.baseUrl + "/items").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(this.itemList.size())))
                .andExpect(jsonPath("$[0].id").value(this.item1.getId()))
                .andExpect(jsonPath("$[1].id").value(this.item2.getId()));
    }
    
    @Test
    void testAddItemSuccess() throws Exception {
        //given
        ItemDTO itemDTO = new ItemDTO(null,
                "i3",
                "yay3", 
                new BigDecimal("43"),
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);

        Item item3 = new Item();
        item3.setId(3);
        item3.setName("i3");
        item3.setCartItems(null);
        item3.setOrderItems(null);
        item3.setDescription("yay3");
        item3.setPrice(new BigDecimal("43"));
        item3.setImageUrl("image");
        item3.setAvailableQuantity(new BigDecimal("3"));


        given(this.itemService.save(Mockito.any(Item.class))).willReturn(item3);

        this.mockMvc.perform(post(this.baseUrl + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item3.getId()))
                .andExpect(jsonPath("$.orderItem").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.name").value(item3.getName()))
                .andExpect(jsonPath("$.description").value(item3.getDescription()))
                .andExpect(jsonPath("$.price").value(item3.getPrice()))
                .andExpect(jsonPath("$.imageUrl").value(item3.getImageUrl()))
                .andExpect(jsonPath("$.availableQuantity").value(item3.getAvailableQuantity()));
    }

    @Test
    void testAddItemBadRequest() throws Exception {
        //given
        ItemDTO itemDTO = new ItemDTO(null,
                null,
                "yay3",
                null,
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);

        Item item3 = new Item();
        item3.setId(3);
        item3.setName(null);
        item3.setCartItems(null);
        item3.setOrderItems(null);
        item3.setDescription("yay3");
        item3.setPrice(null);
        item3.setImageUrl("image");
        item3.setAvailableQuantity(new BigDecimal("3"));


        given(this.itemService.save(Mockito.any(Item.class))).willReturn(item3);

        this.mockMvc.perform(post(this.baseUrl + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("should not be empty/null"))
                .andExpect(jsonPath("$.price").value("should not be empty/null"));
    }
    @Test
    void testUpdateItemSuccess() throws Exception {
        //given
        ItemDTO itemDTO = new ItemDTO(null,
                "i3",
                "yay3",
                new BigDecimal("43"),
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);

        Item item3 = new Item();
        item3.setId(1);
        item3.setName("i3");
        item3.setCartItems(null);
        item3.setOrderItems(null);
        item3.setDescription("yay3");
        item3.setPrice(new BigDecimal("43"));
        item3.setImageUrl("image");
        item3.setAvailableQuantity(new BigDecimal("3"));


        given(this.itemService.update(eq(1),Mockito.any(Item.class))).willReturn(item3);

        this.mockMvc.perform(put(this.baseUrl + "/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item3.getId()))
                .andExpect(jsonPath("$.orderItem").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.name").value(item3.getName()))
                .andExpect(jsonPath("$.description").value(item3.getDescription()))
                .andExpect(jsonPath("$.price").value(item3.getPrice()))
                .andExpect(jsonPath("$.imageUrl").value(item3.getImageUrl()))
                .andExpect(jsonPath("$.availableQuantity").value(item3.getAvailableQuantity()));
    }
    @Test
    void testUpdateItemNotFound () throws Exception{
        //given
        ItemDTO itemDTO = new ItemDTO(null,
                "i3",
                "yay3",
                new BigDecimal("43"),
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);


        given(this.itemService.update(eq(3232),Mockito.any(Item.class))).willThrow(new ObjectNotFoundException("item", 3232));

        this.mockMvc.perform(put(this.baseUrl + "/items/3232")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find item with id 3232"));
    }

    @Test
    void testDeleteItemSuccess () throws Exception{
        doNothing().when(this.itemService).delete(1);

        this.mockMvc.perform(delete(this.baseUrl + "/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Item deleted successfully!"));
    }

    @Test
    void testDeleteItemNotFound () throws Exception {
        doThrow(new ObjectNotFoundException("item", 4)).when(this.itemService).delete(4);

        this.mockMvc.perform(delete(this.baseUrl + "/items/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find item with id 4"));
    }
}