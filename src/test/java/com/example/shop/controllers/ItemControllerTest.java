package com.example.shop.controllers;

import com.example.shop.models.Item;
import com.example.shop.services.ItemService;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
//use embedded h2 database for tests not the postgress db in docker container
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    Item item1;


    @BeforeEach
    void setUp() {
        this.item1 = new Item();
        item1.setId(1);
        item1.setName("a");
        item1.setCartItems(null);
        item1.setOrderItems(null);
        item1.setDescription("yay");
        item1.setPrice(new BigDecimal("32.1"));
        item1.setImageUrl("image");
        item1.setAvailableQuantity(new BigDecimal("2"));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindItemByIdSuccess() throws Exception {
        //Given
        given(this.itemService.findById(1)).willReturn(this.item1);

        //When and then
        this.mockMvc.perform(get("/api/v1/items/1").accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
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

        this.mockMvc.perform(get("/api/v1/items/123").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find item with id 123"));
    }
}