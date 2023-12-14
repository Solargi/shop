package com.example.shop.controllers;

import com.example.shop.dtos.OrderRequestDTO;
import com.example.shop.dtos.OrderResponseDTO;
import com.example.shop.dtos.converters.OrderToOrderDTOConverter;
import com.example.shop.dtos.converters.OrderToOrderRequestDTOConverter;
import com.example.shop.models.Address;
import com.example.shop.models.User;
import com.example.shop.utils.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles({"test","dev"})
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class OrderControllerIntegrationTest {
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

    @Autowired
    OrderToOrderRequestDTOConverter orderToOrderRequestDTOConverter;

    @Autowired
    Login login;

    String token;
    String token2;
    @BeforeEach
    public void setup() throws Exception {
        this.token = login.getJWTToken("u1","q");
        this.token2 = this.login.getJWTToken("u2","f");
    }

    @Test
    @Order(1)
    void testIfContainerIsThere(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    @Order(2)
    void testFindOrderByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/orders/1").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find order with id " + 1));
    }

    @Test
    @Order(3)
    void testAddOrderSuccess() throws Exception {

        this.mockMvc.perform(post(this.baseUrl + "/orders/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalCost").value(170.5))
                .andExpect(jsonPath("$.status").value("processing"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.shippingCost").value(10))
                .andExpect(jsonPath("$.paid").value(true))
                .andExpect(jsonPath("$.shippingAddress.state").value("d"));
    }

    @Test
    @Order(4)
    void testFindOrderByIdSuccess() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orders/1")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalCost").value(170.5))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.status").value("processing"))
                .andExpect(jsonPath("$.shippingAddress.id").value(1));
    }
    @Test
    @Order(5)
    void testFindAllOrderSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/orders")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @Order(6)
    void testAddOrderBadRequest() throws Exception {

        this.mockMvc.perform(post(this.baseUrl + "/orders/1")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                
                .andExpect(jsonPath("$").value("User's cart has no items in it"));
    }

    @Test
    @Order(7)
    void testUpdateOrderSuccess() throws Exception {
        User u1 = new User();
        u1.setId(1);
        u1.setName("a");
        u1.setSurname("b");
        u1.setUsername("u1");
        u1.setPassword("q");
        u1.setEmail("c");
        u1.setRoles("admin");
        u1.setBirthDate("yay");

        Address a1 = new Address();
        a1.setId(1);
        a1.setCountry("a");
        a1.setCity("b");
        a1.setStreet("c");
        a1.setState("state");
        a1.setZipCode(56);
        a1.setUser(u1);
        com.example.shop.models.Order order = new com.example.shop.models.Order();
        order.setStatus("cancelled");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(30));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(a1);
        order.setId(1);


        //given
        OrderRequestDTO orderRequestDTO = this.orderToOrderRequestDTOConverter.convert(order);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.status").value(order.getStatus()))
                .andExpect(jsonPath("$.shippingAddress.state").value(order.getShippingAddress().getState()))
                .andExpect(jsonPath("$.totalCost").value(190.5))
                .andExpect(jsonPath("$.user.id").value(1));

        //check it has been updated in db
        this.mockMvc.perform(get(this.baseUrl + "/orders/1")
                .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                .andExpect(jsonPath("$.shippingCost").value(30));
    }
    @Test
    @Order(8)
    void testUpdateOrderNotFound () throws Exception{
        User u1 = new User();
        u1.setId(1);
        u1.setName("a");
        u1.setSurname("b");
        u1.setUsername("u1");
        u1.setPassword("q");
        u1.setEmail("c");
        u1.setRoles("admin");
        u1.setBirthDate("yay");

        Address a1 = new Address();
        a1.setId(1);
        a1.setCountry("a");
        a1.setCity("b");
        a1.setStreet("c");
        a1.setState("state");
        a1.setZipCode(56);
        a1.setUser(u1);
        com.example.shop.models.Order order = new com.example.shop.models.Order();
        order.setStatus("cancelled");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(30));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(a1);
        order.setId(1);


        //given
        OrderRequestDTO orderRequestDTO = this.orderToOrderRequestDTOConverter.convert(order);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/orders/"+ 32)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find order with id 32"));
    }

    @Test
    @Order(9)
    void testDeleteOrderSuccess () throws Exception{
        this.mockMvc.perform(delete(this.baseUrl + "/orders/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully!"));
    }

    @Test
    @Order(10)
    void testDeleteOrderNotFound () throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/orders/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find order with id 1"));
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD) //reset db
    @Order(11)
    void testAddOrderNotAllowedIfNotOwner() throws Exception {
        this.mockMvc.perform(post(this.baseUrl + "/orders/1")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(13)
    void testAddOrderAllowedIfNotOwnerButAdmin() throws Exception {
        this.mockMvc.perform(post(this.baseUrl + "/orders/2")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.totalCost").value(74.2))
                .andExpect(jsonPath("$.status").value("processing"))
                .andExpect(jsonPath("$.user.id").value(2))
                .andExpect(jsonPath("$.shippingCost").value(10))
                .andExpect(jsonPath("$.paid").value(true))
                .andExpect(jsonPath("$.shippingAddress.state").value("h"));
    }

    @Test
    @Order(12)
    void testAddOrderAllowedIfOwner() throws Exception {

        this.mockMvc.perform(post(this.baseUrl + "/orders/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalCost").value(170.5))
                .andExpect(jsonPath("$.status").value("processing"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.shippingCost").value(10))
                .andExpect(jsonPath("$.paid").value(true))
                .andExpect(jsonPath("$.shippingAddress.state").value("d"));
    }

    @Test
    @Order(14)
    void testFindOrderByIdNotAllowedIfNotOwner() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orders/1")
                        .header("Authorization", token2)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(15)
    void testFindOrderByIdAllowedIfNotOwnerButAdmin() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orders/2")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.totalCost").value(74.2))
                .andExpect(jsonPath("$.user.id").value(2))
                .andExpect(jsonPath("$.status").value("processing"))
                .andExpect(jsonPath("$.shippingAddress.id").value(2));
    }

    @Test
    @Order(16)
    void testFindAllOrderSuccessNowAllowedIfNotAdmin() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/orders")
                        .header("Authorization", token2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(17)
    void testUpdateOrderNotAllowedIfNotAdmin() throws Exception {
        User u1 = new User();
        u1.setId(1);
        u1.setName("a");
        u1.setSurname("b");
        u1.setUsername("u1");
        u1.setPassword("q");
        u1.setEmail("c");
        u1.setRoles("admin");
        u1.setBirthDate("yay");

        Address a1 = new Address();
        a1.setId(1);
        a1.setCountry("a");
        a1.setCity("b");
        a1.setStreet("c");
        a1.setState("state");
        a1.setZipCode(56);
        a1.setUser(u1);
        com.example.shop.models.Order order = new com.example.shop.models.Order();
        order.setStatus("cancelled");
        order.setPaid(true);
        order.setShippingCost(new BigDecimal(30));
        order.setTotalCost(order.computeTotalCost());
        order.setShippingAddress(a1);
        order.setId(1);


        //given
        OrderRequestDTO orderRequestDTO = this.orderToOrderRequestDTOConverter.convert(order);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/orders/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token2))
                
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(18)
    void testDeleteOrderNotAllowedIfNotAdmin () throws Exception{
        this.mockMvc.perform(delete(this.baseUrl + "/orders/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token2))
                
                .andExpect(status().isForbidden());
    }


}
