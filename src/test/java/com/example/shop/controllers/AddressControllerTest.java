package com.example.shop.controllers;

import com.example.shop.dtos.AddressDTO;
import com.example.shop.dtos.converters.AddressDTOToAddressConverter;
import com.example.shop.dtos.converters.AddressToAddressDTOConverter;
import com.example.shop.dtos.converters.UserToUserDTOConverter;
import com.example.shop.models.Address;
import com.example.shop.models.Address;
import com.example.shop.models.User;
import com.example.shop.services.AddressService;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(value = AddressController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
class AddressControllerTest {
    @Value("${api.endpoint.base-url}")
    String baseUrl;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserToUserDTOConverter userToUserDTOConverter;

    @MockBean
    AddressService addressService;


    Address a1 = new Address();
    Address a2 = new Address();

    List<Address> addressesList = new ArrayList<Address>();

    User u1 = new User();

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
        u1.setCartItem(null);

        a1.setId(1);
        a1.setCountry("a");
        a1.setCity("b");
        a1.setStreet("c");
        a1.setState("d");
        a1.setZipCode(56);
        a1.setUser(u1);

        a2.setId(2);
        a2.setCountry("e");
        a2.setCity("f");
        a2.setStreet("g");
        a2.setState("h");
        a2.setZipCode(57);
        a2.setUser(u1);

        addressesList.add(a1);
        addressesList.add(a2);

        u1.setAddresses(addressesList);
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void testFindAddressByIdSuccess() throws Exception {
        //Given
        given(this.addressService.findById(1)).willReturn(this.a1);

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/Addresses/1").accept(MediaType.APPLICATION_JSON))
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
    void testFindAddressByIdNotFound() throws Exception {
        given(this.addressService.findById(123)).willThrow(new ObjectNotFoundException("address",123));

        this.mockMvc.perform(get(this.baseUrl + "/Addresses/123").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find address with id 123"));
    }

    @Test
    void testFindAllAddressSuccess() throws Exception {
        given(this.addressService.findAll()).willReturn(this.addressesList);

        this.mockMvc.perform(get(this.baseUrl + "/addresses").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(this.addressesList.size())))
                .andExpect(jsonPath("$[0].id").value(this.a1.getId()))
                .andExpect(jsonPath("$[1].id").value(this.a2.getId()));
    }
//
    @Test
    void testAddAddressSuccess() throws Exception {
        //given
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


        given(this.addressService.save(Mockito.any(Address.class))).willReturn(a3);

        this.mockMvc.perform(post(this.baseUrl + "/addresses")
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
    void testAddAddressBadRequest() throws Exception {
        //given
        AddressDTO addressDTO = new AddressDTO(null,
                this.userToUserDTOConverter.convert(u1),
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


        given(this.addressService.save(Mockito.any(Address.class))).willReturn(a3);

        this.mockMvc.perform(post(this.baseUrl + "/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddress).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.state").value("must not be empty"))
                .andExpect(jsonPath("$.city").value("must not be empty"));
    }
    @Test
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

        given(this.addressService.update(eq(1),Mockito.any(Address.class))).willReturn(a3);

        this.mockMvc.perform(put(this.baseUrl + "/addresses/1")
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


        given(this.addressService.update(eq(3232),Mockito.any(Address.class))).willThrow(new ObjectNotFoundException("address", 3232));

        this.mockMvc.perform(put(this.baseUrl + "/addresses/3232")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAddress).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find address with id 3232"));
    }

    @Test
    void testDeleteAddressSuccess () throws Exception{
        doNothing().when(this.addressService).delete(1);

        this.mockMvc.perform(delete(this.baseUrl + "/addresses/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Address deleted successfully!"));
    }

    @Test
    void testDeleteAddressNotFound () throws Exception {
        doThrow(new ObjectNotFoundException("address", 4)).when(this.addressService).delete(4);

        this.mockMvc.perform(delete(this.baseUrl + "/addresses/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("could not find address with id 4"));
    }
}