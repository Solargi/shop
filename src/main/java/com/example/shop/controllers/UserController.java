package com.example.shop.controllers;

import com.example.shop.dtos.UserDTO;
import com.example.shop.dtos.converters.UserDTOToUserConverter;
import com.example.shop.dtos.converters.UserToUserDTOConverter;
import com.example.shop.models.User;
import com.example.shop.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    @Parameters({
            @Parameter(name = "userId", description = "The id of the target user", example = "1")
    })
    @Operation(
            summary = "get user data",
            description = "get user data, you can get the user data only of the currently logged in user" +
                    "unless the logged user has an admin role, admins can get information about a specific user",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class),
                                    examples = {
                                            @ExampleObject(name = "user data example",
                                                    value = "{ \"id\": \"1\",\"username\": \"u34\", \"name\": \"paul\", \"surname\": \"surname\", \"email\": \"q@q.com\", \"birthDate\": \"3.1.1991\", \"roles\": \"admin\" }",
                                                    description = "user's data"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "updating another user while having role user",
                                                    description = "trying to update another user using a JWT with role user, " +
                                                            "will result in an empty forbidden response"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "user not found in database",
                                                    value = "could not find user with id 1",
                                                    description = "the user with the provided id does not exist"
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

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser (@PathVariable("userId") int userId){
        User foundUser = this.userService.findById(userId);
        return ResponseEntity.ok(this.userToUserDTOConverter.convert(foundUser));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "get all users data",
            description = "get an array containing all user's data (or an empty one if there is no user), you can get the users data only if " +
                    "the currently logged in user has role admin",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class),
                                    examples = {
                                            @ExampleObject(name = "User List Example",
                                                    value = "[\n" +
                                                            "    {\n" +
                                                            "        \"id\": 1,\n" +
                                                            "        \"username\": \"u1\",\n" +
                                                            "        \"name\": \"a\",\n" +
                                                            "        \"surname\": \"b\",\n" +
                                                            "        \"email\": \"q@q.com\",\n" +
                                                            "        \"birthDate\": \"3.1.1991\",\n" +
                                                            "        \"roles\": \"admin\"\n" +
                                                            "    },\n" +
                                                            "    {\n" +
                                                            "        \"id\": 2,\n" +
                                                            "        \"username\": \"u2\",\n" +
                                                            "        \"name\": \"a\",\n" +
                                                            "        \"surname\": \"b\",\n" +
                                                            "        \"email\": \"q@q.com\",\n" +
                                                            "        \"birthDate\": \"3.1.1991\",\n" +
                                                            "        \"roles\": \"user\"\n" +
                                                            "    }\n" +
                                                            "]",
                                                    description = "array containing user's data"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "not admin or unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "updating another user while having role user",
                                                    description = "trying to update another user using a JWT with role user, " +
                                                            "will result in an empty forbidden response"
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
                                            value = "{  \"username\": \"u1\", \"name\": \"a\", \"surname\": \"b\", \"addresses\": [], \"email\": \"q@q.com\", \"password\": \"1\", \"birthDate\": \"3.1.1991\", \"cartItems\": [], \"orderList\": [], \"roles\": \"admin\" }",
                                            description = "create an admin account"
                                    ),
                                    @ExampleObject(name = "User",
                                            value = "{  \"username\": \"u2\", \"name\": \"b\", \"surname\": \"c\", \"addresses\": [], \"email\": \"a@a.com\", \"password\": \"2\", \"birthDate\": \"11.12.1992\", \"cartItems\": [], \"orderList\": [], \"roles\": \"user\" }",
                                            description = "create normal user account")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class),
                                    examples = {
                                            @ExampleObject(name = "created user",
                                                    value = "{ \"id\": \"1\",\"username\": \"u34\", \"name\": \"paul\", \"surname\": \"surname\", \"email\": \"q@q.com\", \"birthDate\": \"3.1.1991\", \"roles\": \"admin\" }",
                                                    description = "created user's data"
                                            )
                                    }
                            )
                    ),
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
                                            value = "{  \"username\": \"u34\", \"name\": \"paul\", \"surname\": \"surname\", \"email\": \"q@q.com\", \"birthDate\": \"3.1.1991\", \"roles\": \"admin\" }",
                                            description = "modifies user account"
                                    ),
                                    @ExampleObject(name = "Modify user",
                                            value = "{  \"username\": \"u2\", \"name\": \"b\", \"surname\": \"c\", \"addresses\": [], \"email\": \"a@a.com\", \"password\": \"2\", \"birthDate\": \"11.12.1992\", \"cartItems\": [], \"orderList\": [], \"roles\": \"user\" }",
                                            description = "create normal user account")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class),
                                    examples = {
                                            @ExampleObject(name = "modified user's data example",
                                                    value = "{  \"id\": \"1\",\"username\": \"u34\", \"name\": \"paul\", \"surname\": \"surname\", \"email\": \"q@q.com\", \"birthDate\": \"3.1.1991\", \"roles\": \"admin\" }",
                                                    description = "modified user's data"
                                            )
                                    }
                            )
                    ),
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
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "not admin or unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "updating another user while having role user",
                                                    description = "trying to update another user using a JWT with role user, " +
                                                            "will result in an empty forbidden response"
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
    @Parameters({
            @Parameter(name = "userId", description = "The id of the target user", example = "1",
                    in = ParameterIn.PATH)
    })
    @Operation(
            summary = "Delete User",
            description = "you can only delete the currently logged in user unless the logged in users has " +
                    "role admin, admins can delete whoever. trying to delete another user with an account having role user" +
                    "will cause a 403 forbidden response.",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = UserDTO.class),
                                    examples = {
                                            @ExampleObject(name = "successful delete",
                                                    value = "user deleted successfully!"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "not admin or unauthorized",
                                                    description = "you will get an unauthorized response if you are not " +
                                                            "logged in, meaning that " +
                                                            "you didn't provide a JWT or you provided an invalid/expired one"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(
                                                    name = "deleting another user while having role user",
                                                    description = "trying to delete another user using a JWT with role user, " +
                                                            "will result in an empty forbidden response"
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "text",
                                    examples = {
                                            @ExampleObject(
                                                    name = "user not found in database",
                                                    value = "could not find user with id 1",
                                                    description = "the user with the provided id does not exist"
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
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return ResponseEntity.ok("user deleted successfully!");
    }

}
