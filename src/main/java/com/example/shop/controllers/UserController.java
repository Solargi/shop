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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<User> users = this.userService.findAll();
        List<UserDTO> usersDTO = users.stream().map(this.userToUserDTOConverter::convert).toList();
        return ResponseEntity.ok(usersDTO);
    }

    @Operation(
            summary = "Create a new user",
            //need full qualifier for swagger otherwise it will overwrite spring's request body
            //since it has the same name
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = {
                                    @ExampleObject(name = "Admin",
                                            value = "{ \"username\": \"u1\", \"name\": \"a\", \"surname\": \"b\", \"addresses\": [null], \"email\": \"q@q.com\", \"password\": \"1\", \"birthDate\": \"3.1.1991\", \"cartItems\": [], \"orderList\": [], \"roles\": \"admin\" }",
                                            description = "create an admin account"
                                    ),
                                    @ExampleObject(name = "User",
                                            value = "{ \"username\": \"u2\", \"name\": \"b\", \"surname\": \"c\", \"addresses\": [null], \"email\": \"a@a.com\", \"password\": \"2\", \"birthDate\": \"11.12.1992\", \"cartItems\": [], \"orderList\": [], \"roles\": \"user\" }",
                                            description = "create normal user account")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "missing or empty requiered fields",
                                                    value = "{\n" +
                                                            "    \"password\": \"must not be empty\",\n" +
                                                            "    \"surname\": \"must not be empty\",\n" +
                                                            "    \"roles\": \"must not be empty\",\n" +
                                                            "    \"name\": \"must not be empty\",\n" +
                                                            "    \"birthDate\": \"must not be empty\",\n" +
                                                            "    \"email\": \"must not be empty\",\n" +
                                                            "    \"username\": \"must not be empty\"\n" +
                                                            "}",
                                                    description = "(the response can either be empty or contain one or more of the listed elements)"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "internal Server Error", content = {
                            @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "some error",
                                                    value = "either empty or a string with the error",
                                                    description = "this response body can contain either the error text or be empty"
                                            ),
                                    }
                            ),
                            @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "empty",
                                                    summary = "empty response"
                                            ),
                                    }
                            )
                    }
                    )
            }

    )

    @PostMapping("")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody User user){
        User savedUser = this.userService.save(user);
        UserDTO savedUserDTO = this.userToUserDTOConverter.convert(savedUser);
        return ResponseEntity.ok(savedUserDTO);
    }

    @Operation(
            summary = "modify existing user",
            description = "to modify an existing user you must either " +
                    "be an admin or be logged in with the user you want to modify",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = {
                                    @ExampleObject(name = "modify admin",
                                            value = "{ \"username\": \"u34\", \"name\": \"paul\", \"surname\": \"surname\", \"email\": \"q@q.com\", \"birthDate\": \"3.1.1991\", \"roles\": \"admin\" }",
                                            description = "modifies user account"
                                    ),
                                    @ExampleObject(name = "Modify user",
                                            value = "{ \"username\": \"u2\", \"name\": \"b\", \"surname\": \"c\", \"addresses\": [null], \"email\": \"a@a.com\", \"password\": \"2\", \"birthDate\": \"11.12.1992\", \"cartItems\": [], \"orderList\": [], \"roles\": \"user\" }",
                                            description = "create normal user account")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "missing or empty requiered fields",
                                                    value = "{\n" +
                                                            "    \"surname\": \"should not be empty/null\",\n" +
                                                            "    \"roles\": \"should not be empty/null\",\n" +
                                                            "    \"name\": \"should not be empty/null\",\n" +
                                                            "    \"birthDate\": \"should not be empty/null\",\n" +
                                                            "    \"email\": \"should not be empty/null\",\n" +
                                                            "    \"username\": \"should not be empty/null\"\n" +
                                                            "}",
                                                    description = "(the response can either be empty or contain one or more of the listed elements)"
                                            )

                                    }
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDTO userDTO){
        User user = this.userDTOToUserConverter.convert(userDTO);
        //can't be null since initial user DTO must be validated @Valid
        User updatedUser = this.userService.update(userId,user);
        UserDTO updatedUserDTO = this.userToUserDTOConverter.convert(updatedUser);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return ResponseEntity.ok("user deleted successfully!");
    }

}
