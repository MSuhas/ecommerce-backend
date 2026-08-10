package com.suhasm.ecommerce.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductRequestDTO {

    @NotBlank(message = "name cannot be blank")
    private String name;

    @Size(min = 10, message = "description cannot be blank and must contain at least 10 characters")
    private String description;
    @Positive(message = "price must be greater than zero")
    private BigDecimal price;
    @PositiveOrZero(message = "stock cannot be negative")
    private Integer stock;

    @NotNull
    @Positive(message = "categoryId must be greater than zero")
    private Long categoryId;
}