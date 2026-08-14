package com.suhasm.ecommerce.dto;

import com.suhasm.ecommerce.entity.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private List<OrderItemResponseDTO> items;
    private LocalDateTime createdAt;
}
