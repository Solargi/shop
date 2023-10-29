package com.example.shop.controllers;

import com.example.shop.dtos.UserDTO;
import com.example.shop.models.Address;
import com.example.shop.models.User;
import com.example.shop.services.UserService;
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

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
//use embedded h2 database for tests not the postgress db in docker container
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc

class UserControllerTest {
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;


    User u1 = new User();
    User u2 = new User();
    List<User> usersList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        u1.setId(1);
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

        u2.setId(1);
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

        usersList.add(u1);
        usersList.add(u2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindItemByIdSuccess() throws Exception {
        //Given
        given(this.userService.findById(1)).willReturn(u1);

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(u1.getId()))
                .andExpect(jsonPath("$.addresses").doesNotExist())
                .andExpect(jsonPath("$.cartItems").doesNotExist())
                .andExpect(jsonPath("$.orderList").doesNotExist())
                .andExpect(jsonPath("$.name").value(u1.getName()))
                .andExpect(jsonPath("$.surname").value(u1.getSurname()))
                .andExpect(jsonPath("$.username").value(u1.getUsername()))
                .andExpect(jsonPath("$.email").value(u1.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.birthDate").value(u1.getBirthDate()));
    }

    @Test
    void TestFindUserByIdNotFound() throws Exception {
        given(this.userService.findById(123)).willThrow(new ObjectNotFoundException("user",123));
        this.mockMvc.perform(get(this.baseUrl + "/users/123").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find user with id 123"));
    }

    @Test
    void testFindAllUsersSuccess() throws Exception{
        given(this.userService.findAll()).willReturn(this.usersList);

        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(this.usersList.size())))
                .andExpect(jsonPath("$[0].id").value(this.u1.getId()))
                .andExpect(jsonPath("$[1].id").value(this.u2.getId()));
    }

    @Test
    void testAddUserSuccess() throws Exception{
        Address a3 = new Address();
        a3.setId(1);
        a3.setCountry("a");
        a3.setCity("b");
        a3.setStreet("c");
        a3.setState("d");
        a3.setZipCode(56);
        a3.setUser(u1);
        List<Address> addresses = new ArrayList<Address>();
        addresses.add(a3);
        User u3 = new User();
        u3.setId(6);
        u3.setName("a");
        u3.setSurname("b");
        u3.setUsername("u1");
        u3.setPassword("q");
        u3.setEmail("c");
        u3.setRoles("admin");
        u3.setBirthDate("yay");
        //TODO: ASSIGN THEM TO ACTUAL OBJECTS
        u3.setAddresses(addresses);
        u3.setOrderList(null);
        u3.setCartItems(null);

        given(this.userService.save(Mockito.any(User.class))).willReturn(u3);
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

    }

    @Test
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
        //TODO: ASSIGN THEM TO ACTUAL OBJECTS
        u3.setOrderList(null);
        u3.setCartItems(null);
        u3.setAddresses(null);

        given(this.userService.save(Mockito.any(User.class))).willReturn(u3);
        String jsonUser = this.objectMapper.writeValueAsString(u3);
        System.out.println(jsonUser);

        this.mockMvc.perform(post(this.baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.password").value("must not be null"))
                .andExpect(jsonPath("$.email").value("must not be empty"));
    }

    @Test
    void testUpdateUserSuccess() throws Exception{
        UserDTO userDTO = new UserDTO(
                4, "q","a","k",
                "a@a.com","yay","admin");

        String jsonUser = this.objectMapper.writeValueAsString(userDTO);

        User u3 = new User();
        u3.setId(1);
        u3.setName("a");
        u3.setSurname("b");
        u3.setUsername("u1");
        u3.setPassword("q");
        u3.setEmail("c");
        u3.setRoles("admin");
        u3.setBirthDate("yay");
        //TODO: ASSIGN THEM TO ACTUAL OBJECTS
        u3.setAddresses(null);
        u3.setOrderList(null);
        u3.setCartItems(null);

        given(this.userService.update(eq(1),Mockito.any(User.class))).willReturn(u3);

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

    }

    @Test
    void testUpdateUserNotFound()throws Exception{
        UserDTO userDTO = new UserDTO(
                4, "q","a","k",
                "a@a.com","yay","admin");

        String jsonUser = this.objectMapper.writeValueAsString(userDTO);

        User u3 = new User();
        u3.setId(4);
        u3.setName("a");
        u3.setSurname("b");
        u3.setUsername("u1");
        u3.setPassword("q");
        u3.setEmail("c");
        u3.setRoles("admin");
        u3.setBirthDate("yay");
        u3.setAddresses(null);
        u3.setOrderList(null);
        u3.setCartItems(null);

        given(this.userService.update(eq(3232),Mockito.any(User.class))).willThrow(new ObjectNotFoundException("user",3232));

        this.mockMvc.perform(put(this.baseUrl + "/users/3232")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find user with id 3232"));

    }

    @Test
    void testDeleteUserSuccess () throws Exception {
        doNothing().when(this.userService).delete(6);

        this.mockMvc.perform(delete(this.baseUrl + "/users/6")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("user deleted successfully!"));
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        doThrow(new ObjectNotFoundException("user", 6)).when(this.userService).delete(6);

        this.mockMvc.perform(delete(this.baseUrl + "/users/6")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find user with id 6"));
    }


}