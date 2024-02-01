package com.example.shop.controllers;

import com.example.shop.dtos.UserDTO;
import com.example.shop.dtos.converters.UserDTOToUserConverter;
import com.example.shop.dtos.converters.UserToUserDTOConverter;
import com.example.shop.models.User;
import com.example.shop.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "Create a new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = {
                                    @ExampleObject(name = "Admin User",
                                            value = "{ \"username\": \"u2\", \"name\": \"a\", \"surname\": \"b\", \"addresses\": [null], \"email\": \"q@q.com\", \"password\": \"1\", \"birthDate\": \"yay\", \"cartItems\": [], \"orderList\": [], \"roles\": \"admin\" }")
                            }
                    )
            )
    )

    @PostMapping("")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody User user){
        User savedUser = this.userService.save(user);
        UserDTO savedUserDTO = this.userToUserDTOConverter.convert(savedUser);
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
