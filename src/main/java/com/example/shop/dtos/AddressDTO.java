package com.example.shop.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddressDTO (Integer id,
                          @NotNull
                          UserDTO userDTO,
                          @NotEmpty
                          String country,
                          @NotEmpty
                          String state,
                           @NotEmpty
                          String city,
                          @NotEmpty
                          String street,
                          @NotNull
                          Integer zipCode) {
}
