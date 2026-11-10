package com.suhasm.ecommerce.dto;

public record ReleaseStockRequest(
        Integer quantity,
        String reservationId
) {}
