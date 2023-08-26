package com.example.shop.dtos;

import jakarta.validation.constraints.NotEmpty;

public record UserDTO (Integer id,
                       @NotEmpty(message = "username is required.")
                       String username,
                       @NotEmpty(message = "roles are required.")
                       String roles){
}
