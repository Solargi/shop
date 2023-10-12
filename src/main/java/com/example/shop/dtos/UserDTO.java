package com.example.shop.dtos;


import jakarta.validation.constraints.NotEmpty;



public record UserDTO (Integer id,
                       @NotEmpty(message = "should not be empty/null")
                       String username,
                       @NotEmpty(message = "should not be empty/null")
                       String name,
                       @NotEmpty(message = "should not be empty/null")
                       String surname,
                       @NotEmpty(message = "should not be empty/null")
                       String email,
                       @NotEmpty(message = "should not be empty/null")
                       String birthDate,
                       @NotEmpty(message = "should not be empty/null")
                       String roles) {
}

