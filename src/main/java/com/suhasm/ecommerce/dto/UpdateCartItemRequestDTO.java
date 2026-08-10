package com.suhasm.ecommerce.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCartItemRequestDTO {
    @Min(1)
    private Integer quantity;
}
