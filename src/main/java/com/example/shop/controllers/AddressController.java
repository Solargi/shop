package com.example.shop.controllers;

import com.example.shop.dtos.AddressDTO;
import com.example.shop.dtos.AddressRequestDTO;
import com.example.shop.dtos.UserDTO;
import com.example.shop.dtos.converters.AddressRequestDTOToAddressConverter;
import com.example.shop.dtos.converters.AddressToAddressDTOConverter;
import com.example.shop.dtos.converters.AddressDTOToAddressConverter;
import com.example.shop.models.Address;
import com.example.shop.services.AddressService;
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
@RequestMapping("${api.endpoint.base-url}/addresses")
@AllArgsConstructor
public class AddressController {
    private AddressToAddressDTOConverter addressToAddressDTOConverter;
    private AddressDTOToAddressConverter addressDTOToAddressConverter;
    private AddressRequestDTOToAddressConverter addressRequestDTOToAddressConverter;
    private AddressService addressService;

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "AddressId", description = "The id of the target address", example = "1")
    })
    @Operation(
            summary = "get address data",
            description = "get address data, you can get the address data only of the currently logged in user" +
                    "unless the logged user has an admin role, admins can get information about a specific address",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AddressDTO.class),
                                    examples = {
                                            @ExampleObject(name = "address data example",
                                                    value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"userDTO\": {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"username\": \"u1\",\n" +
                                                    "    \"name\": \"a\",\n" +
                                                    "    \"surname\": \"b\",\n" +
                                                    "    \"email\": \"q@q.com\",\n" +
                                                    "    \"birthDate\": \"3.1.1991\",\n" +
                                                    "    \"roles\": \"admin\"\n" +
                                                    "  },\n" +
                                                    "  \"country\": \"Switzerland\",\n" +
                                                    "  \"state\": \"Zurich\",\n" +
                                                    "  \"city\": \"Zurich\",\n" +
                                                    "  \"street\": \"Zurich strasse\",\n" +
                                                    "  \"zipCode\": 8942\n" +
                                                    "}",
                                                    description = "address data"
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
                                                    name = "fetching another user's address while having role user",
                                                    description = "trying to fetch another user's address using a JWT with role user, " +
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
                                                    name = "address not found in database",
                                                    value = "could not find address with id 1",
                                                    description = "the address with the provided id does not exist"
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

    @GetMapping("/{addressId}")
    ResponseEntity<AddressDTO> getAddress(@PathVariable Integer addressId){
        Address address = this.addressService.findById(addressId);
        return ResponseEntity.ok(this.addressToAddressDTOConverter.convert(address));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "get all addresses data",
            description = "get an array containing all addresses data (or an empty one if there is no address), you can get the addresses data only if " +
                    "the currently logged in user has role admin",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AddressDTO.class),
                                    examples = {
                                            @ExampleObject(name = "Address List Example",
                                                    value = "[\n" +
                                                            "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "  \"country\": \"Switzerland\",\n" +
                                                            "  \"state\": \"Zurich\",\n" +
                                                            "  \"city\": \"Zurich\",\n" +
                                                            "  \"street\": \"Zurich strasse\",\n" +
                                                            "  \"zipCode\": 8942\n" +
                                                            "},\n" +
                                                            "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "  \"country\": \"Italy\",\n" +
                                                            "  \"state\": \"Provincia di Foggia\",\n" +
                                                            "  \"city\": \"Neviano\",\n" +
                                                            "  \"street\": \"strada 2\",\n" +
                                                            "  \"zipCode\": 39372\n" +
                                                            "}\n" +
                                                            "]",
                                                    description = "array containing addresses data"
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
                                                    name = "fetching addresses while having role user",
                                                    description = "trying to fetch the addresses using a JWT with role user, " +
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
    ResponseEntity<List<AddressDTO>> getAddresses (){
        return ResponseEntity.ok(this.addressService.findAll()
                .stream()
                .map(this.addressToAddressDTOConverter::convert)
                .toList());
    }
    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "userId", description = "The id the user that " +
                    "will be associated with the new address", example = "1")
    })
    @Operation(
            summary = "Create a new address",
            description = "an address can be created and associated only to the currently logged in " +
                    "user, unless the user has role admin, admins can create addresses associated to any user",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AddressRequestDTO.class),
                            examples = {
                                    @ExampleObject(name = "Address 1",
                                            value = "{\n" +
                                                    "  \"country\": \"Switzerland\",\n" +
                                                    "  \"state\": \"Zurich\",\n" +
                                                    "  \"city\": \"Zurich\",\n" +
                                                    "  \"street\": \"Zurich strasse\",\n" +
                                                    "  \"zipCode\": 8942\n" +
                                                    "}",
                                            description = "create an address associated to the user" +
                                                    " having the provided UserID"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AddressDTO.class),
                                    examples = {
                                            @ExampleObject(name = "address data example",
                                                    value = "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "  \"country\": \"Switzerland\",\n" +
                                                            "  \"state\": \"Zurich\",\n" +
                                                            "  \"city\": \"Zurich\",\n" +
                                                            "  \"street\": \"Zurich strasse\",\n" +
                                                            "  \"zipCode\": 8942\n" +
                                                            "}",
                                                    description = "address data"
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
                                                            "    \"country\": \"must not be empty\",\n" +
                                                            "    \"state\": \"must not be empty\",\n" +
                                                            "    \"city\": \"must not be empty\",\n" +
                                                            "    \"street\": \"must not be empty\",\n" +
                                                            "    \"zipCode\": \"must not be empty\"" +
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
                                                    name = "fetching addresses while having role user",
                                                    description = "trying to fetch the addresses using a JWT with role user, " +
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

    @PostMapping("/{userId}")
    ResponseEntity<AddressDTO> saveAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO,
                                           @PathVariable Integer userId){
        Address address = this.addressRequestDTOToAddressConverter.convert(addressRequestDTO);
        //TODO rework address save controller and service fetch user in service save
        // remove user dto use userId is enough
        Address savedAddress = this.addressService.save(address, userId);
        return ResponseEntity.ok(this.addressToAddressDTOConverter.convert(savedAddress));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "addressId", description = "The id of the target address", example = "1")
    })
    @Operation(
            summary = "Update an existing address",
            description = "an address can be updated and associated only to the currently logged in " +
                    "user, unless the user has role admin, admins can update addresses associated to any user",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AddressDTO.class),
                            examples = {
                                    @ExampleObject(name = "Address 1",
                                            value = "{\n" +
                                                    "  \"userDTO\": {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"username\": \"u1\",\n" +
                                                    "    \"name\": \"a\",\n" +
                                                    "    \"surname\": \"b\",\n" +
                                                    "    \"email\": \"q@q.com\",\n" +
                                                    "    \"birthDate\": \"3.1.1991\",\n" +
                                                    "    \"roles\": \"admin\"\n" +
                                                    "  },\n" +
                                                    "  \"country\": \"Switzerland\",\n" +
                                                    "  \"state\": \"Zurich\",\n" +
                                                    "  \"city\": \"Zurich\",\n" +
                                                    "  \"street\": \"Zurich strasse\",\n" +
                                                    "  \"zipCode\": 8942\n" +
                                                    "}",
                                            description = "update the address associated with the " +
                                                    "target id"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AddressDTO.class),
                                    examples = {
                                            @ExampleObject(name = "address data example",
                                                    value = "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"userDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "  \"country\": \"Switzerland\",\n" +
                                                            "  \"state\": \"Zurich\",\n" +
                                                            "  \"city\": \"Zurich\",\n" +
                                                            "  \"street\": \"Zurich strasse\",\n" +
                                                            "  \"zipCode\": 8942\n" +
                                                            "}",
                                                    description = "address data"
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
                                                            "    \"country\": \"must not be empty\",\n" +
                                                            "    \"state\": \"must not be empty\",\n" +
                                                            "    \"city\": \"must not be empty\",\n" +
                                                            "    \"street\": \"must not be empty\",\n" +
                                                            "    \"zipCode\": \"must not be empty\"" +
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
                                                    name = "fetching addresses while having role user",
                                                    description = "trying to update the addresses using a JWT with role user, " +
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
                                            ),
                                            @ExampleObject(
                                                    name = "address not found in database",
                                                    value = "could not find address with id 1",
                                                    description = "the address with the provided id does not exist"
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
    @PutMapping("/{addressId}")
    ResponseEntity<AddressDTO> updateAddress (@PathVariable Integer addressId, @Valid @RequestBody AddressDTO addressDTO){
        Address address = this.addressDTOToAddressConverter.convert(addressDTO);
        Address updatedAddress = this.addressService.update(addressId,address);
        AddressDTO updatedAddressDTO = this.addressToAddressDTOConverter.convert(updatedAddress);
        return ResponseEntity.ok(updatedAddressDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "addressId", description = "The id of the target address", example = "1",
                    in = ParameterIn.PATH)
    })
    @Operation(
            summary = "Delete Address",
            description = "you can only delete the currently logged in user's address unless the logged in users has " +
                    "role admin, admins can delete all addresses. trying to delete another user's address with an account having role user" +
                    "will cause a 403 forbidden response.",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "*/*",
                                    examples = {
                                            @ExampleObject(name = "successful delete",
                                                    value = "address deleted successfully!"
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
                                                    name = "deleting another user's address while having role user",
                                                    description = "trying to delete another user's address using a JWT with role user, " +
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
                                                    name = "address not found in database",
                                                    value = "could not find address with id 1",
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

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress (@PathVariable Integer addressId){
        this.addressService.delete(addressId);
        return ResponseEntity.ok("Address deleted successfully!");
    }

}
