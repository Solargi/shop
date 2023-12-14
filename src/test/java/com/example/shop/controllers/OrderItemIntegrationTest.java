package com.example.shop.controllers;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.dtos.OrderItemRequestDTO;
import com.example.shop.models.OrderItem;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import com.example.shop.utils.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
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

import static org.assertj.core.api.Assertions.assertThat;
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

@ActiveProfiles({"test","dev"})
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderItemIntegrationTest {
    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    public static void  overrideProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Login login;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    OrderItemId oid1 = new OrderItemId(1,2);

    String token;
    String token2;
    @BeforeEach
    public void setup() throws Exception {
        this.token = this.login.getJWTToken("u1","q");
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
    void testCreateOrder()throws Exception{
        this.mockMvc.perform(post(this.baseUrl + "/orders/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(post(this.baseUrl + "/orders/2")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(3)
    void testFindOrderItemByIdSuccess() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orderItems/1/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(1))
                .andExpect(jsonPath("$.id.itemId").value(1))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(10))
                .andExpect(jsonPath("$.totalCost").value(160))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    @Order(4)
    void testFindOrderItemByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/orderItems/1/5")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id OrderItemId(orderId=1, itemId=5)"));
    }

    @Test
    @Order(5)
    void testFindAllOrderItemSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/orderItems")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(new OrderItemId(1,1)))
                .andExpect(jsonPath("$[1].id").value(new OrderItemId(2,2)));
    }

    @Test
    @Order(6)
    void testAddOrderItemSuccess() throws Exception {

        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(10);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);


        this.mockMvc.perform(post(this.baseUrl + "/orderItems/" + oid1.getOrderId() +"/" + oid1.getItemId())
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(1))
                .andExpect(jsonPath("$.id.itemId").value(2))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(10))
                .andExpect(jsonPath("$.totalCost").value(321))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    @Order(7)
    void testAddOrderItemBadRequest() throws Exception {
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(0);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        this.mockMvc.perform(post(this.baseUrl + "/orderItems/42/0" )
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.quantity").value("must be greater than 0"));
    }
    @Test
    @Order(8)
    void testUpdateOrderItemSuccess() throws Exception {
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(1);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/orderItems/" + oid1.getOrderId() + "/" + oid1.getItemId())
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(oid1.getOrderId()))
                .andExpect(jsonPath("$.id.itemId").value(oid1.getItemId()))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(10))
                .andExpect(jsonPath("$.totalCost").value(32))
                .andExpect(jsonPath("$.quantity").value(1));
    }
    @Test
    @Order(9)
    void testUpdateOrderItemNotFound () throws Exception{
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(1);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);


        this.mockMvc.perform(put(this.baseUrl + "/orderItems/32/12")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id " + new OrderItemId(32,12)));
    }

    @Test
    @Order(10)
    void testDeleteOrderItemSuccess () throws Exception{
        this.mockMvc.perform(delete(this.baseUrl + "/orderItems/1/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(content().string("OrderItem deleted successfully!"));
    }

    @Test
    @Order(11)
    void testDeleteOrderItemNotFound () throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/orderItems/1/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id " + new OrderItemId(1,1)));
    }


    @Test
    @Order(12)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testResetOrder()throws Exception{
        this.mockMvc.perform(post(this.baseUrl + "/orders/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(post(this.baseUrl + "/orders/2")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(13)
    void testFindOrderItemByIdNotAllowedIfNotOwner() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orderItems/1/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token2))
                
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(14)
    void testFindOrderItemByIdAllowedIfOwnerAndNotAdmin() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orderItems/2/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token2))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(2))
                .andExpect(jsonPath("$.id.itemId").value(2))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(10))
                .andExpect(jsonPath("$.totalCost").value(64.2))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    @Order(15)
    void testFindOrderItemByIdAllowedIfNotOwnerButAdmin() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orderItems/2/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(2))
                .andExpect(jsonPath("$.id.itemId").value(2))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(10))
                .andExpect(jsonPath("$.totalCost").value(64.2))
                .andExpect(jsonPath("$.quantity").value(2));
    }
    @Test
    @Order(16)
    void testFindAllOrderItemNotAllowedIfNotAdmin() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/orderItems")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(17)
    void testAddOrderItemNotAllowedIfNotAdmin() throws Exception {

        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(10);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);


        this.mockMvc.perform(post(this.baseUrl + "/orderItems/2/2" )
                        .header("Authorization", this.token2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(18)
    void testUpdateOrderItemNotAllowedIfNotAdmin() throws Exception {
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(1);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/orderItems/2/2")
                        .header("Authorization", this.token2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(19)
    void testDeleteOrderItemNotAllowedIfNotAdmin () throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/orderItems/2/2")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
    }

}
