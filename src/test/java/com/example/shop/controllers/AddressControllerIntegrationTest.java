package com.example.shop.controllers;

import com.example.shop.dtos.AddressDTO;
import com.example.shop.dtos.AddressRequestDTO;
import com.example.shop.dtos.converters.UserToUserDTOConverter;
import com.example.shop.models.Address;
import com.example.shop.models.User;
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
public class AddressControllerIntegrationTest {
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

    @Autowired
    UserToUserDTOConverter userToUserDTOConverter;

    @Autowired
    Login login;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    Address a1 = new Address();
    User u1 = new User();

    String token;


    @BeforeEach
    public void setup() throws Exception {
        this.token = this.login.getJWTToken("u1","q");
        u1.setId(1);
        u1.setName("a");
        u1.setSurname("b");
        u1.setUsername("u1");
        u1.setPassword("q");
        u1.setEmail("c");
        u1.setRoles("admin");
        u1.setBirthDate("yay");

        a1.setId(1);
        a1.setCountry("a");
        a1.setCity("b");
        a1.setStreet("c");
        a1.setState("d");
        a1.setZipCode(56);
        a1.setUser(u1);

    }

    @Test
    @Order(1)
    void testIfContainerIsThere() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }



    @Test
    @Order(2)
    void testFindAddressByIdSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/addresses/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(a1.getId()))
                .andExpect(jsonPath("$.street").value(a1.getStreet()))
                .andExpect(jsonPath("$.city").value(a1.getCity()))
                .andExpect(jsonPath("$.state").value(a1.getState()))
                .andExpect(jsonPath("$.country").value(a1.getCountry()))
                .andExpect(jsonPath("$.zipCode").value(a1.getZipCode()))
                .andExpect(jsonPath("$.userDTO.id").value(u1.getId()));
    }

    @Test
    @Order(3)
    void testFindAddressByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/addresses/123")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find address with id 123"));
    }

    @Test
    @Order(4)
    void testFindAllAddressSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/addresses")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(this.a1.getId()))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @Order(5)
    void testAddAddressSuccess() throws Exception {
        //id shoud be set correctly by db
        AddressRequestDTO addressDTO = new AddressRequestDTO(
                "a",
                "d",
                "b",
                "c",
                56);
        //convert dto to json mockmvc can't send the DTO object
        String jsonAddress = this.objectMapper.writeValueAsString(addressDTO);

        Address a3 = new Address();
        a3.setId(3);
        a3.setCountry("a");
        a3.setCity("b");
        a3.setStreet("c");
        a3.setState("d");
        a3.setZipCode(56);
        a3.setUser(u1);


        this.mockMvc.perform(post(this.baseUrl + "/addresses/1")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddress).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(a3.getId()))
                .andExpect(jsonPath("$.street").value(a3.getStreet()))
                .andExpect(jsonPath("$.city").value(a3.getCity()))
                .andExpect(jsonPath("$.state").value(a3.getState()))
                .andExpect(jsonPath("$.country").value(a3.getCountry()))
                .andExpect(jsonPath("$.zipCode").value(a3.getZipCode()))
                .andExpect(jsonPath("$.userDTO.id").value(u1.getId()));
    }

    @Test
    @Order(6)
    void testFindAddedAddressByIdSuccess() throws Exception {
        AddressDTO addressDTO = new AddressDTO(3,
                this.userToUserDTOConverter.convert(u1),
                "a",
                "d",
                "b",
                "c",
                56);
        //convert dto to json mockmvc can't send the DTO object
        String jsonAddress = this.objectMapper.writeValueAsString(addressDTO);

        Address a3 = new Address();
        a3.setId(3);
        a3.setCountry("a");
        a3.setCity("b");
        a3.setStreet("c");
        a3.setState("d");
        a3.setZipCode(56);
        a3.setUser(u1);

        this.mockMvc.perform(get(this.baseUrl + "/addresses/3")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(a3.getId()))
                .andExpect(jsonPath("$.street").value(a3.getStreet()))
                .andExpect(jsonPath("$.city").value(a3.getCity()))
                .andExpect(jsonPath("$.state").value(a3.getState()))
                .andExpect(jsonPath("$.country").value(a3.getCountry()))
                .andExpect(jsonPath("$.zipCode").value(a3.getZipCode()))
                .andExpect(jsonPath("$.userDTO.id").value(u1.getId()));
    }


    @Test
    @Order(7)
    void testAddAddressBadRequest() throws Exception {
        AddressRequestDTO addressDTO = new AddressRequestDTO(
                "a",
                "",
                null,
                "c",
                56);
        //convert dto to json mockmvc can't send the DTO object
        String jsonAddress = this.objectMapper.writeValueAsString(addressDTO);

        Address a3 = new Address();
        a3.setId(3);
        a3.setCountry("a");
        a3.setCity("b");
        a3.setStreet("c");
        a3.setState("d");
        a3.setZipCode(56);
        a3.setUser(u1);


        this.mockMvc.perform(post(this.baseUrl + "/addresses/1")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddress).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.state").value("must not be empty"))
                .andExpect(jsonPath("$.city").value("must not be empty"));
    }
    @Test
    @Order(8)
    void testUpdateAddressSuccess() throws Exception {
        //given
        AddressDTO addressDTO = new AddressDTO(1,
                this.userToUserDTOConverter.convert(u1),
                "a",
                "d",
                "b",
                "c",
                56);
        //convert dto to json mockmvc can't send the DTO object
        String jsonAddress = this.objectMapper.writeValueAsString(addressDTO);

        Address a3 = new Address();
        a3.setId(1);
        a3.setCountry("a");
        a3.setCity("b");
        a3.setStreet("c");
        a3.setState("d");
        a3.setZipCode(56);
        a3.setUser(u1);

        this.mockMvc.perform(put(this.baseUrl + "/addresses/1")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddress).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(a3.getId()))
                .andExpect(jsonPath("$.street").value(a3.getStreet()))
                .andExpect(jsonPath("$.city").value(a3.getCity()))
                .andExpect(jsonPath("$.state").value(a3.getState()))
                .andExpect(jsonPath("$.country").value(a3.getCountry()))
                .andExpect(jsonPath("$.zipCode").value(a3.getZipCode()))
                .andExpect(jsonPath("$.userDTO.id").value(u1.getId()));
    }
    @Test
    @Order(9)
    void testUpdateAddressNotFound () throws Exception{
        //given
        AddressDTO addressDTO = new AddressDTO(1,
                this.userToUserDTOConverter.convert(u1),
                "a",
                "d",
                "b",
                "c",
                56);
        //convert dto to json mockmvc can't send the DTO object
        String jsonAddress = this.objectMapper.writeValueAsString(addressDTO);

        this.mockMvc.perform(put(this.baseUrl + "/addresses/3232")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token)
                        .content(jsonAddress).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find address with id 3232"));
    }

    @Test
    @Order(10)
    void test3TotalAddressesBeforeDeleting() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/addresses")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Order(11)
    void testDeleteAddressSuccess () throws Exception{
        this.mockMvc.perform(delete(this.baseUrl + "/addresses/1")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Address deleted successfully!"));
    }

    @Test
    @Order(12)
    void test2TotalAddressesAfterDeleting() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/addresses")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(13)
    void testDeleteAddressNotFound () throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/addresses/4")
                        .header("Authorization", this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find address with id 4"));
    }
}
