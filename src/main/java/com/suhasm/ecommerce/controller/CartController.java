package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.AddCartItemRequestDTO;
import com.suhasm.ecommerce.dto.CartResponseDTO;
import com.suhasm.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem(@Valid @RequestBody AddCartItemRequestDTO request) {

        log.info("Adding item to cart : {}", request);

        return ResponseEntity.ok(cartService.addItem(request));

    }
}
