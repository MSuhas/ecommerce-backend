package com.suhasm.ecommerce.dto;

import java.math.BigDecimal;

public record ReserveStockResponse(
        Long productId,
        Integer reservedQuantity,
        Integer remainingStock,
        String productName,
        BigDecimal unitPrice
) {
}
