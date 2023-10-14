package com.example.shop.services;

import com.example.shop.models.Address;
import com.example.shop.models.Address;
import com.example.shop.models.User;
import com.example.shop.repositories.AddressRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    AddressRepository addressRepository;

    @InjectMocks
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
    void findByIdSuccess(){
        //define behaviour of mock object
        given(addressRepository.findById(1)).willReturn(Optional.of(a1));

        // When. Act on target behavior. when steps should cover the method to be tested
        Address returnedAddress = addressService.findById(1);


        // Then. Assert expected outcomes.
        assertThat(returnedAddress.getId()).isEqualTo(a1.getId());
        assertThat(returnedAddress.getStreet()).isEqualTo(a1.getStreet());
        assertThat(returnedAddress.getCity()).isEqualTo(a1.getCity());
        assertThat(returnedAddress.getState()).isEqualTo(a1.getState());
        assertThat(returnedAddress.getCountry()).isEqualTo(a1.getCountry());
        assertThat(returnedAddress.getZipCode()).isEqualTo(a1.getZipCode());
        assertThat(returnedAddress.getUser()).isEqualTo(a1.getUser());
    }

    @Test
    void findByIdNotFound(){
        //Given
        given(addressRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            Address returnedAddress = addressService.findById(14);});

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("could not find address with id 14");
    }

    @Test
    void testFindAllSuccess(){
//        given
        given(addressRepository.findAll()).willReturn(this.addressesList);
//        when
        List<Address> foundAddresss = addressService.findAll();
//        then
        assertThat(foundAddresss.size()).isEqualTo(this.addressesList.size());
        verify(addressRepository, times(1)).findAll();

    }

    @Test
    void testSaveSuccess(){
        Address a3 = new Address();
        a3.setId(3);
        a3.setCountry("a");
        a3.setCity("b");
        a3.setStreet("c");
        a3.setState("d");
        a3.setZipCode(56);
        a3.setUser(u1);

        given(addressRepository.save(a3)).willReturn(a3);

        Address savedAddress = addressService.save(a3);
        assertThat(savedAddress.getId()).isEqualTo(a3.getId());
        assertThat(savedAddress.getStreet()).isEqualTo(a3.getStreet());
        assertThat(savedAddress.getCity()).isEqualTo(a3.getCity());
        assertThat(savedAddress.getState()).isEqualTo(a3.getState());
        assertThat(savedAddress.getCountry()).isEqualTo(a3.getCountry());
        assertThat(savedAddress.getZipCode()).isEqualTo(a3.getZipCode());
        assertThat(savedAddress.getUser()).isEqualTo(a3.getUser());
    }

    @Test
    void testUpdateSuccess(){
        Address a3 = new Address();
        a3.setId(1);
        a3.setCountry("a");
        a3.setCity("b");
        a3.setStreet("c");
        a3.setState("d");
        a3.setZipCode(12);
        a3.setUser(u1);
        given(addressRepository.findById(1)).willReturn(Optional.of(a1));
        //when savind address 1 already has the new values
        given(addressRepository.save(a1)).willReturn(a1);



        Address updated1 = addressService.update(a3.getId(),a3);

        assertThat(updated1.getId()).isEqualTo(a1.getId());
        assertThat(updated1.getZipCode()).isEqualTo(a3.getZipCode());
        verify(addressRepository, times(1)).findById(1);
        verify(addressRepository, times(1)).save(a1);

    }

    @Test
    void testUpdateNotFound(){
        given(addressRepository.findById(3232)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            addressService.update(3232,a1);
        });

        verify(addressRepository,times(1)).findById(3232);

    }
    @Test
    void testDeleteSuccess(){
        given(addressRepository.findById(1)).willReturn(Optional.of(a1));
        doNothing().when(addressRepository).deleteById(1);

        addressService.delete(1);

        verify(addressRepository, times(1)).deleteById(1);


    }

    @Test
    void testDeleteNotFound(){
        given(addressRepository.findById(4)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            addressService.delete(4);
        });

        verify(addressRepository, times(1)).findById(4);


    }
}    