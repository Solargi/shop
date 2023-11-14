package com.example.shop.controllers;


import com.example.shop.dtos.UserDTO;
import com.example.shop.models.Address;
import com.example.shop.models.User;
import com.example.shop.system.exceptions.ObjectNotFoundException;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles({"test","dev"})
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserControllerIntegrationTest {
    //setup postgres test container
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry){
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
    void testIfContainerIsThere(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    @Order(2)
    void testFindUserByIdSuccess() throws Exception {

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.addresses").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.orderList").doesNotExist())
                .andExpect(jsonPath("$.name").value("a"))
                .andExpect(jsonPath("$.surname").value("b"))
                .andExpect(jsonPath("$.username").value("u1"))
                .andExpect(jsonPath("$.email").value("c"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.birthDate").value("yay"))
                .andExpect(jsonPath("$.roles").value("admin"));
    }

    @Test
    @Order(3)
    void TestFindUserByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/users/123").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find user with id 123"));
    }

    @Test
    @Order(4)
    void testFindAllUsersSuccess() throws Exception{
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @Order(5)
    void testAddUserSuccess() throws Exception{
        Address a3 = new Address();
        a3.setId(3);
        a3.setCountry("a");
        a3.setCity("b");
        a3.setStreet("c");
        a3.setState("d");
        a3.setZipCode(56);

        User u3 = new User();
        u3.setId(3);
        u3.setName("a");
        u3.setSurname("b");
        u3.setUsername("u3");
        u3.setPassword("q");
        u3.setEmail("c");
        u3.setRoles("admin");
        u3.setBirthDate("yay");
        a3.setUser(u3);


        String jsonUser = this.objectMapper.writeValueAsString(u3);

        this.mockMvc.perform(post(this.baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(u3.getId()))
                .andExpect(jsonPath("$.addresses").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.orderList").doesNotExist())
                .andExpect(jsonPath("$.name").value(u3.getName()))
                .andExpect(jsonPath("$.surname").value(u3.getSurname()))
                .andExpect(jsonPath("$.username").value(u3.getUsername()))
                .andExpect(jsonPath("$.email").value(u3.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.birthDate").value(u3.getBirthDate()));
        this.mockMvc.perform(get(this.baseUrl + "/users/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void testAddUserBadRequest() throws Exception {

        User u3 = new User();
        u3.setId(6);
        u3.setName("a");
        u3.setSurname("b");
        u3.setUsername("u1");
        u3.setPassword(null);
        u3.setEmail("");
        u3.setRoles("admin");
        u3.setBirthDate("yay");

        String jsonUser = this.objectMapper.writeValueAsString(u3);
        System.out.println(jsonUser);

        this.mockMvc.perform(post(this.baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.password").value("must not be empty"))
                .andExpect(jsonPath("$.email").value("must not be empty"));
    }

    @Test
    @Order(7)
    void testUpdateUserSuccess() throws Exception{
        UserDTO userDTO = new UserDTO(
                4, "q","a","k",
                "a@a.com","yay","user");

        String jsonUser = this.objectMapper.writeValueAsString(userDTO);

        User u3 = new User();
        u3.setId(1);
        u3.setName("a");
        u3.setSurname("k");
        u3.setUsername("q");
        u3.setPassword("q");
        u3.setEmail("a@a.com");
        u3.setRoles("user");
        u3.setBirthDate("yay");
        u3.setAddresses(null);
        u3.setOrderList(null);
        u3.setCartItems(null);


        this.mockMvc.perform(put(this.baseUrl + "/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(u3.getId()))
                .andExpect(jsonPath("$.addresses").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.orderList").doesNotExist())
                .andExpect(jsonPath("$.name").value(u3.getName()))
                .andExpect(jsonPath("$.surname").value(u3.getSurname()))
                .andExpect(jsonPath("$.username").value(u3.getUsername()))
                .andExpect(jsonPath("$.email").value(u3.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.birthDate").value(u3.getBirthDate()));
        //check update has been saved
        this.mockMvc.perform(get(this.baseUrl + "/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.username").value(u3.getUsername()));

    }

    @Test
    @Order(8)
    void testUpdateUserNotFound()throws Exception{
        UserDTO userDTO = new UserDTO(
                4, "q","a","k",
                "a@a.com","yay","admin");

        String jsonUser = this.objectMapper.writeValueAsString(userDTO);


        this.mockMvc.perform(put(this.baseUrl + "/users/3232")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find user with id 3232"));
    }
    @Test
    @Order(9)
    void testDeleteUserSuccess () throws Exception {
        //check that cartItems before deleting related user
        this.mockMvc.perform(get(this.baseUrl+"/cartItems")
                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)));

        this.mockMvc.perform(delete(this.baseUrl + "/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("user deleted successfully!"));
        //check that cartItems are deleted after the related user is deleted
        this.mockMvc.perform(get(this.baseUrl+"/cartItems")
                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    @Order(10)
    void testDeleteUserNotFound() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/users/6")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find user with id 6"));
    }
}
