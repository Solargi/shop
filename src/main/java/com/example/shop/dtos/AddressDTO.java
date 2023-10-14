package com.example.shop.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddressDTO (Integer id,
                          @NotNull
                          UserDTO userDTO,
                          @NotNull @NotEmpty
                          String country,
                          @NotNull @NotEmpty
                          String state,
                          @NotNull @NotEmpty
                          String city,
                          @NotNull @NotEmpty
                          String street,
                          @NotNull
                          Integer zipCode) {
}
