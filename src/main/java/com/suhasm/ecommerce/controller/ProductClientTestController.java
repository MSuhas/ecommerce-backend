package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.client.ProductClient;
import com.suhasm.ecommerce.dto.ReserveStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/product")
@RequiredArgsConstructor
public class ProductClientTestController {

    private final ProductClient productClient;

    @PostMapping("/{productId}/reserve/{quantity}")
    public ReserveStockResponse reserve(
            @PathVariable Long productId,
            @PathVariable int quantity,
            @PathVariable String reserveId) {

        return productClient.reserveStock(productId, quantity, reserveId);
    }
}