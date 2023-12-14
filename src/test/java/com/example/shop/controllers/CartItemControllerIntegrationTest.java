package com.example.shop.controllers;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.dtos.CartItemRequestDTO;
import com.example.shop.dtos.CartItemResponseDTO;
import com.example.shop.dtos.converters.CartItemToCartItemResponseDTOConverter;
import com.example.shop.models.CartItem;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

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
public class CartItemControllerIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry){
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

    String token;
    String token2;


    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @BeforeEach
    public void setup() throws Exception {
        this.token = this.login.getJWTToken("u1","q");
        this.token2 = this.login.getJWTToken("u2","f");
    }
    @Test
    @Order(1)
    void testFindCartItemByIdSuccess() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/1/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.userId").value(1))
                .andExpect(jsonPath("$.id.itemId").value(1))
                .andExpect(jsonPath("$.userDTO.id").value(1))
                .andExpect(jsonPath("$.itemDTO.id").value(1))
                .andExpect(jsonPath("$.quantity").value("5"));
    }

    @Test
    @Order(2)
    void testFindCartItemByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/3/108")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find cartItem with id CartItemId(userId=3, itemId=108)"));
    }

    @Test
    @Order(3)
    void testFindAllCartItemSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/cartItems")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id.userId").value(1))
                .andExpect(jsonPath("$[1].id.userId").value(2));
    }
    //
    @Test
    @Order(4)
    void testAddCartItemSuccess() throws Exception {
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(1,2);
        ci3.setId(cid3);
        ci3.setQuantity(1);
        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(10);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);





        this.mockMvc.perform(post(this.baseUrl + "/cartItems/"
                        + ci3.getId().getUserId() + "/"
                        + ci3.getId().getItemId())
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ci3.getId()))
                .andExpect(jsonPath("$.itemDTO.id").value(2))
                .andExpect(jsonPath("$.userDTO.id").value(1))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.id.userId").value(ci3.getId().getUserId()))
                .andExpect(jsonPath("$.id.itemId").value(ci3.getId().getItemId()))
                .andExpect(jsonPath("$.totalCost").value(321));
    }
    //
    @Test
    @Order(5)
    void testAddCartItemBadRequest() throws Exception {
        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(0);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);


        this.mockMvc.perform(post(this.baseUrl + "/cartItems/54444/34")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                
                .andExpect(jsonPath("$.quantity").value("must be greater than 0"));
    }
//
//    //TODO add test for adding items with invalid item id , user id
    @Test
    @Order(6)
    void testUpdateCartItemSuccess() throws Exception {
        CartItemId cid3 = new CartItemId(1,1);

        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(20);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/cartItems/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cid3))
                .andExpect(jsonPath("$.itemDTO.id").value(1))
                .andExpect(jsonPath("$.userDTO.id").value(1))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.id.userId").value(1))
                .andExpect(jsonPath("$.id.itemId").value(1))
                .andExpect(jsonPath("$.totalCost").value(642));
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/1/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.quantity").value(20));
    }
    @Test
    @Order(7)
    void testUpdateCartItemNotFound () throws Exception{
        CartItemId cid3 = new CartItemId(1,1);

        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(20);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/cartItems/3233/2222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find cartItem with id CartItemId(userId=3233, itemId=2222)"));
    }

    @Test
    @Order(8)
    void testDeleteCartItemSuccess () throws Exception{
        this.mockMvc.perform(delete(this.baseUrl + "/cartItems/1/2")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(content().string("CartItem deleted successfully!"));
        //check for deletion
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/1/2")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find cartItem with id CartItemId(userId=1, itemId=2)"));
    }

    @Test
    @Order(9)
    void testDeleteCartItemNotFound () throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/cartItems/32/12")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find cartItem with id CartItemId(userId=32, itemId=12)"));
    }

    @Test
    @Order(10)
    void testFindOtherUsersCartItemByIdNotAllowedIfNotOwnerOrAdmin() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/1/1")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(11)
    void testFindOtherUsersCartItemByIdAllowedIfOwner() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/2/2")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.userId").value(2))
                .andExpect(jsonPath("$.id.itemId").value(2))
                .andExpect(jsonPath("$.userDTO.id").value(2))
                .andExpect(jsonPath("$.itemDTO.id").value(2))
                .andExpect(jsonPath("$.quantity").value("2"));
    }

    @Test
    @Order(12)
    void testFindOtherUsersCartItemByIdAllowedIfAdmin() throws Exception {
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/2/2")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.userId").value(2))
                .andExpect(jsonPath("$.id.itemId").value(2))
                .andExpect(jsonPath("$.userDTO.id").value(2))
                .andExpect(jsonPath("$.itemDTO.id").value(2))
                .andExpect(jsonPath("$.quantity").value("2"));
    }
    @Test
    @Order(13)
    void testFindAllCartItemNotAllowedIfNotAdmin() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/cartItems")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(14)
    void testAddCartItemNotAllowedIfNotOwner() throws Exception {
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(1,2);
        ci3.setId(cid3);
        ci3.setQuantity(1);
        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(10);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);


        this.mockMvc.perform(post(this.baseUrl + "/cartItems/1/2")
                        .header("Authorization", this.token2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(15)
    void testAddCartItemNotAllowedIfNotSignInUserInDTOButObjectIsCorrectedBeforeSaving() throws Exception {
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(1,2);
        ci3.setId(cid3);
        ci3.setQuantity(1);
        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(10);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);


        this.mockMvc.perform(post(this.baseUrl + "/cartItems/2/2")
                        .header("Authorization", this.token2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.userId").value(2))
                .andExpect(jsonPath("$.userDTO.id").value(2));
    }

    @Test
    @Order(16)
    void testAddCartItemAllowedIfNotOwnerButAdmin() throws Exception {
        CartItem ci3 = new CartItem();
        CartItemId cid3 = new CartItemId(2,2);
        ci3.setId(cid3);
        ci3.setQuantity(1);
        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(10);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);


        this.mockMvc.perform(post(this.baseUrl + "/cartItems/2/2")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.userId").value(2))
                .andExpect(jsonPath("$.userDTO.id").value(2));
    }

    @Test
    @Order(17)
    void testUpdateCartItemNotAllowedIfNotOwner() throws Exception {
        CartItemId cid3 = new CartItemId(1,1);

        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(20);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/cartItems/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token2)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/1/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    @Order(18)
    void testUpdateCartItemAllowedIfNotOwnerButAdmin() throws Exception {
        CartItemId cid3 = new CartItemId(2,2);

        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(20);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/cartItems/2/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cid3))
                .andExpect(jsonPath("$.itemDTO.id").value(2))
                .andExpect(jsonPath("$.userDTO.id").value(2))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.id.userId").value(2))
                .andExpect(jsonPath("$.id.itemId").value(2))
                .andExpect(jsonPath("$.totalCost").value(642));
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/2/2")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    @Order(19)
    void testUpdateCartItemAllowedIfOwner() throws Exception {
        CartItemId cid3 = new CartItemId(2,2);

        //given
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(20);
        //convert dto to json mockmvc can't send the DTO object
        String jsonItem = this.objectMapper.writeValueAsString(cartItemRequestDTO);

        this.mockMvc.perform(put(this.baseUrl + "/cartItems/2/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token2)
                        .content(jsonItem).accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cid3))
                .andExpect(jsonPath("$.itemDTO.id").value(2))
                .andExpect(jsonPath("$.userDTO.id").value(2))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.id.userId").value(2))
                .andExpect(jsonPath("$.id.itemId").value(2))
                .andExpect(jsonPath("$.totalCost").value(642));
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/2/2")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    @Order(20)
    void testDeleteCartItemNotAllowedIfNotOwnerOrAdmin () throws Exception{
        this.mockMvc.perform(delete(this.baseUrl + "/cartItems/1/1")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isForbidden());
        //check for deletion
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/1/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk());
    }

    @Test
    @Order(21)
    void testDeleteAllowedIfOwner () throws Exception{
        this.mockMvc.perform(delete(this.baseUrl + "/cartItems/2/2")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk());
        //check for deletion
        this.mockMvc.perform(get(this.baseUrl + "/cartItems/2/2")
                        .header("Authorization", this.token2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find cartItem with id CartItemId(userId=2, itemId=2)"));
    }
    //delete if admin already done in normal delete
}
