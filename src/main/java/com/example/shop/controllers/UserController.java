package com.example.shop.controllers;

import com.example.shop.dtos.UserDTO;
import com.example.shop.dtos.converters.UserDTOToUserConverter;
import com.example.shop.dtos.converters.UserToUserDTOConverter;
import com.example.shop.models.User;
import com.example.shop.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDTOToUserConverter userDTOToUserConverter;
    private final UserToUserDTOConverter userToUserDTOConverter;


    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser (@PathVariable("userId") int userId){
        User foundUser = this.userService.findById(userId);
        return ResponseEntity.ok(this.userToUserDTOConverter.convert(foundUser));
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<User> users = this.userService.findAll();
        List<UserDTO> usersDTO = users.stream().map(this.userToUserDTOConverter::convert).toList();
        return ResponseEntity.ok(usersDTO);
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO){
        User user = this.userDTOToUserConverter.convert(userDTO);
        user = this.userService.save(user);
        UserDTO savedUserDTO = this.userToUserDTOConverter.convert(user);
        return ResponseEntity.ok(savedUserDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDTO userDTO){
        User user = this.userDTOToUserConverter.convert(userDTO);
        //can't be null since initial user DTO must be validated @Valid
        User updatedUser = this.userService.update(userId,user);
        UserDTO updatedUserDTO = this.userToUserDTOConverter.convert(updatedUser);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return ResponseEntity.ok("user deleted successfully!");
    }

}
