package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.AddCartItemRequestDTO;
import com.suhasm.ecommerce.dto.CartResponseDTO;
import com.suhasm.ecommerce.dto.UpdateCartItemRequestDTO;
import com.suhasm.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<CartResponseDTO> getItems() {
        log.info("Getting item from cart :");

        return ResponseEntity.ok(cartService.getCart());
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CartResponseDTO> updateItem(@Valid @RequestBody UpdateCartItemRequestDTO requestDTO,
                                                      @PathVariable Long id) {
        log.info("Update item in cart :{}", requestDTO);

        return ResponseEntity.ok(cartService.updateQuantity(id, requestDTO));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<CartResponseDTO> removeItem(@PathVariable Long id) {
        log.info("Deleting item from Cart");

        return ResponseEntity.ok(cartService.removeItem(id));
    }

    @DeleteMapping()
    public ResponseEntity<CartResponseDTO> clearCart() {
        log.info("Clearing  Cart");

        return ResponseEntity.ok(cartService.clearCart());
    }
}
