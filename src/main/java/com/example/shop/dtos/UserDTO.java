package com.example.shop.dtos;


import jakarta.validation.constraints.NotEmpty;



public record UserDTO (Integer id,
                       @NotEmpty(message = "username is required.")
                       String username,
                       String name,
                       String surname,
                       String email,
                       String birthDate,
                       @NotEmpty(message = "roles are required.")
                       String roles) {
}

