package com.suhasm.ecommerce.dto;

public record ReleaseStockResponse(
        Long productId,
        Integer releasedQuantity,
        Integer currentStock
) {}
