package com.example.shop.controllers;

import com.example.shop.dtos.ItemDTO;
import com.example.shop.models.Item;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test","dev"})
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
//run test in order as they appear in the code
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//some tests depends on others run the whole class at once
// this class uses the same db for all tests
// remove static from container if a new container per test is preferred
// DBDataInit will add data to this this db
// To cancel that use @Profile("!test") on Dbinit
// and @ActiveProfile("test") here
public class ItemControllerIntegrationTest {
    @Container
    //create container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    //overwrite propreties in applicaiton.yml at runtime
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        //overwrite data source with the url of the container that is generated at random
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @Test
    @Order(1)
    void testIfContainerIsThere() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    @Order(2)
    void testFindAllItemsSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get(this.baseUrl + "/items")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(3)
    void testFindItemByIdSuccess() throws Exception {
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
    @Order(4)
    void testFindItemByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/items/3").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find item with id 3"));
    }

    @Test
    @Order(5)
    void testAddItemSuccess() throws Exception {
        ItemDTO itemDTO = new ItemDTO(null,
                "i3",
                "yay3",
                new BigDecimal("43"),
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);

        Item item3 = new Item();
        item3.setName("i3");
        item3.setCartItems(null);
        item3.setOrderItems(null);
        item3.setDescription("yay3");
        item3.setPrice(new BigDecimal("43"));
        item3.setImageUrl("image");
        item3.setAvailableQuantity(new BigDecimal("3"));

        this.mockMvc.perform(post(this.baseUrl + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderItem").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.name").value(item3.getName()))
                .andExpect(jsonPath("$.description").value(item3.getDescription()))
                .andExpect(jsonPath("$.price").value(item3.getPrice()))
                .andExpect(jsonPath("$.imageUrl").value(item3.getImageUrl()))
                .andExpect(jsonPath("$.availableQuantity").value(item3.getAvailableQuantity()));
    }

    @Test
    @Order(6)
    void testItemFoundByIdAfterAddingIt() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/items/3").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderItem").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.name").value("i3"))
                .andExpect(jsonPath("$.description").value("yay3"))
                .andExpect(jsonPath("$.price").value(43))
                .andExpect(jsonPath("$.imageUrl").value("image"))
                .andExpect(jsonPath("$.availableQuantity").value(3));
    }

    @Test
    @Order(7)
    void testAddItemWithExistingIdBadRequest() throws Exception {
        ItemDTO itemDTO = new ItemDTO(3,
                "i3",
                "yay3",
                new BigDecimal("43"),
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);
        System.out.println(jsonItem);


        this.mockMvc.perform(post(this.baseUrl + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
//                .andDo(print())
                .andExpect(jsonPath("$")
                        .value("item with id "
                                + itemDTO.id() + " already exists"));
    }

    @Test
    @Order(8)
    void testAddItemWithInvalidDTOBadRequest() throws Exception {
        ItemDTO itemDTO = new ItemDTO(3,
                "",
                "",
                new BigDecimal("43"),
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);
        System.out.println(jsonItem);


        this.mockMvc.perform(post(this.baseUrl + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
//                .andDo(print())
                .andExpect(jsonPath("$.name")
                        .value("should not be empty/null"));
    }

    @Test
    @Order(9)
    void testUpdateItemSuccess() throws Exception {
        //given
        //notice that even if the id is wrong in the dto
        //the item should be updated correctly keeping original id
        ItemDTO itemDTO = new ItemDTO(43,
                "i3",
                "yay3",
                new BigDecimal("43"),
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);


        this.mockMvc.perform(put(this.baseUrl + "/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderItem").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.name").value(itemDTO.name()))
                .andExpect(jsonPath("$.description").value(itemDTO.description()))
                .andExpect(jsonPath("$.price").value(itemDTO.price()))
                .andExpect(jsonPath("$.imageUrl").value(itemDTO.imageUrl()))
                .andExpect(jsonPath("$.availableQuantity").value(itemDTO.availableQuantity()));
        //check updated version is in db
        this.mockMvc.perform(get(this.baseUrl + "/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name").value(itemDTO.name()));
    }

    @Test
    @Order(10)
    void testUpdateItemNotFound() throws Exception {
        //given
        ItemDTO itemDTO = new ItemDTO(null,
                "i3",
                "yay3",
                new BigDecimal("43"),
                "image",
                new BigDecimal(3));
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(itemDTO);

        this.mockMvc.perform(put(this.baseUrl + "/items/3232")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find item with id 3232"));
    }

    @Test
    @Order(11)
    void testDeleteItemSuccess () throws Exception{
        //check child entities dimension before deleting item
        // this should be separated in another test but this saves time
        this.mockMvc.perform(get(this.baseUrl+"/cartItems")
                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)));

        this.mockMvc.perform(delete(this.baseUrl + "/items/1")
                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Item deleted successfully!"));
        //check child entities dimensions after deleting items
        //cart items should be deleted as well when deleting item
        this.mockMvc.perform(get(this.baseUrl+"/cartItems")
                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Order(12)
    void testDeleteItemNotFound () throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/items/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find item with id 4"));
    }



}