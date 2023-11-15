package com.example.shop.controllers;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.dtos.OrderItemRequestDTO;
import com.example.shop.models.OrderItem;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    OrderItemId oid1 = new OrderItemId(1,2);

    @Test
    @Order(1)
    void testIfContainerIsThere(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    @Order(2)
    void testCreateOrder()throws Exception{
        this.mockMvc.perform(post(this.baseUrl + "/orders/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(post(this.baseUrl + "/orders/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(3)
    void testFindOrderItemByIdSuccess() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/orderItems/1/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
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
        this.mockMvc.perform(get(this.baseUrl + "/orderItems/1/5").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id OrderItemId(orderId=1, itemId=5)"));
    }

    @Test
    @Order(5)
    void testFindAllOrderItemSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/orderItems").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(new OrderItemId(1,1)))
                .andExpect(jsonPath("$[1].id").value(new OrderItemId(2,2)));
    }

    @Test
    @Order(6)
    void testAddOrderItemSuccess() throws Exception {

        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(oid1,10);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);


        this.mockMvc.perform(post(this.baseUrl + "/orderItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(1))
                .andExpect(jsonPath("$.id.itemId").value(2))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(10))
                .andExpect(jsonPath("$.totalCost").value(321))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void testAddOrderItemBadRequest() throws Exception {
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(null,0);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        this.mockMvc.perform(post(this.baseUrl + "/orderItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.id").value("must not be null"))
                .andExpect(jsonPath("$.quantity").value("must be greater than 0"));
    }
    @Test
    @Order(8)
    void testUpdateOrderItemSuccess() throws Exception {
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(oid1,1);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/orderItems/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.orderId").value(oid1.getOrderId()))
                .andExpect(jsonPath("$.id.itemId").value(oid1.getItemId()))
                .andExpect(jsonPath("$.orderResponseDTO.shippingCost").value(10))
                .andExpect(jsonPath("$.totalCost").value(32))
                .andExpect(jsonPath("$.quantity").value(1));
    }
    @Test
    void testUpdateOrderItemNotFound () throws Exception{
        //given
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(new OrderItemId(32,12),1);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(orderItemRequestDTO);


        this.mockMvc.perform(put(this.baseUrl + "/orderItems/32/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id " + new OrderItemId(32,12)));
    }

    @Test
    @Order(9)
    void testDeleteOrderItemSuccess () throws Exception{
        this.mockMvc.perform(delete(this.baseUrl + "/orderItems/1/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("OrderItem deleted successfully!"));
    }

    @Test
    @Order(10)
    void testDeleteOrderItemNotFound () throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/orderItems/1/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find orderItem with id " + new OrderItemId(1,1)));
    }


}
