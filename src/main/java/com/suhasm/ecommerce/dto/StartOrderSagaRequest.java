package com.suhasm.ecommerce.dto;

public record StartOrderSagaRequest(
        Long productId,
        Integer quantity
) {}
