package com.suhasm.ecommerce.dto;

import com.suhasm.ecommerce.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequestDTO {

    @NotNull(message = "status cannot be null")
    private OrderStatus status;
}
