package com.suhasm.ecommerce.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private Long userId;
    private String eventId;
    private BigDecimal totalAmount;
}