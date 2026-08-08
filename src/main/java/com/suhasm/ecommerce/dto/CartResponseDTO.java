package com.suhasm.ecommerce.dto;

import com.suhasm.ecommerce.entity.CartItem;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartResponseDTO {

    private Long cartId;

    private List<CartItemResponseDTO> items;

    private BigDecimal totalAmount;
}
