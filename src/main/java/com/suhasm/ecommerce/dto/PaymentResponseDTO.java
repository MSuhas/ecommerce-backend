package com.suhasm.ecommerce.dto;

import com.suhasm.ecommerce.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long paymentId;
    private Long orderId;
    private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
