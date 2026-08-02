package com.suhasm.ecommerce.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryRequestDTO {


    @NotBlank(message = "Category name is required.")
    @Size(max = 100, message = "Category name cannot exceed 100 characters.")
    private String name;
}
