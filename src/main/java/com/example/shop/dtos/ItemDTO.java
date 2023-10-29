package com.example.shop.dtos;



import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemDTO(
                      Integer id,
                      @NotEmpty(message = "should not be empty/null")
                      String name,
                      String description,
                      @NotNull(message = "should not be empty/null")
                      BigDecimal price,
                      String imageUrl,
                      @NotNull(message = "should not be empty/null")
                      BigDecimal availableQuantity) {


}