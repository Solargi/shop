package com.example.shop.services;

import com.example.shop.models.User;
import com.example.shop.repositories.UserRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)


class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
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

        u2.setId(2);
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
    void testFindByIdSuccess(){
        given(this.userRepository.findById(1)).willReturn(Optional.of(u1));

        User foundUser = this.userService.findById(1);
        assertThat(foundUser.getId()).isEqualTo(u1.getId());
        assertThat(foundUser.getName()).isEqualTo(u1.getName());
        assertThat(foundUser.getSurname()).isEqualTo(u1.getSurname());
        assertThat(foundUser.getUsername()).isEqualTo(u1.getUsername());
        assertThat(foundUser.getEmail()).isEqualTo(u1.getEmail());
        assertThat(foundUser.getBirthDate()).isEqualTo(u1.getBirthDate());
        assertThat(foundUser.getRoles()).isEqualTo(u1.getRoles());
        assertThat(foundUser.getCartItems()).isEqualTo(u1.getCartItems());
        assertThat(foundUser.getPassword()).isEqualTo(u1.getPassword());
        assertThat(foundUser.getAddresses()).isEqualTo(u1.getAddresses());
        assertThat(foundUser.getOrderList()).isEqualTo(u1.getOrderList());
    }

    @Test
    void testFindByIdNotFound(){
        given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            User foundUser = userService.findById(14);});

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("could not find User with id 14");
    }

    @Test
    void testFindAllSuccess(){
//        given
        given(this.userRepository.findAll()).willReturn(this.usersList);
//        when
        List<User> foundUsers = this.userService.findAll();
//        then
        assertThat(foundUsers.size()).isEqualTo(this.usersList.size());
        verify(this.userRepository, times(1)).findAll();

    }

    @Test
    void testSaveSuccess(){
        User u3 = new User();
        u3.setId(3);
        u3.setName("d");
        u3.setSurname("e");
        u3.setUsername("u3");
        u3.setPassword("password");
        u3.setEmail("g");
        u3.setRoles("user");
        u3.setBirthDate("yay3");
        //TODO: ASSIGN THEM TO ACTUAL OBJECTS
        u3.setAddresses(null);
        u3.setOrderList(null);
        u3.setCartItems(null);

        given(this.userRepository.save(u3)).willReturn(u3);

        User savedUser = this.userService.save(u3);
        assertThat(savedUser.getId()).isEqualTo(u3.getId());
        assertThat(savedUser.getName()).isEqualTo(u3.getName());
        assertThat(savedUser.getSurname()).isEqualTo(u3.getSurname());
        assertThat(savedUser.getUsername()).isEqualTo(u3.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(u3.getEmail());
        assertThat(savedUser.getBirthDate()).isEqualTo(u3.getBirthDate());
        assertThat(savedUser.getRoles()).isEqualTo(u3.getRoles());
        assertThat(savedUser.getCartItems()).isEqualTo(u3.getCartItems());
    }

    @Test
    void testUpdateSuccess(){
         User u3 = new User();
         u3.setId(1);
         u3.setName("d");
         u3.setSurname("e");
         u3.setUsername("u3");
         u3.setPassword("password");
         u3.setEmail("g");
         u3.setRoles("user");
         u3.setBirthDate("yay3");
         //TODO: ASSIGN THEM TO ACTUAL OBJECTS
         u3.setAddresses(null);
         u3.setOrderList(null);
         u3.setCartItems(null);

         given(this.userRepository.findById(1)).willReturn(Optional.of(u1));
         //when saving item 1 already has the new values
         given(this.userRepository.save(u1)).willReturn(u1);

         User updated1 = this.userService.update(u3.getId(),u3);

         assertThat(updated1.getId()).isEqualTo(u1.getId());
         assertThat(updated1.getUsername()).isEqualTo(u3.getUsername());
         verify(this.userRepository, times(1)).findById(1);
         verify(this.userRepository, times(1)).save(u1);

    }

    @Test
    void testUpdateNotFound(){
        given(this.userRepository.findById(3232)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            this.userService.update(3232,u1);
        });

        verify(this.userRepository,times(1)).findById(3232);

    }
    @Test
    void testDeleteSuccess(){
        given(this.userRepository.findById(1)).willReturn(Optional.of(u1));
        doNothing().when(this.userRepository).deleteById(1);

        this.userService.delete(1);

        verify(this.userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound(){
        given(this.userRepository.findById(4)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()->{
            this.userService.delete(4);
        });

        verify(this.userRepository, times(1)).findById(4);
    }


}