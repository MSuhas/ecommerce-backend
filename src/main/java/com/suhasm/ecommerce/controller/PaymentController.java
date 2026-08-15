package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.PaymentResponseDTO;
import com.suhasm.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<PaymentResponseDTO> createPayment(@PathVariable Long orderId) {

        return ResponseEntity.ok(paymentService.createPayment(orderId));
    }

    @PatchMapping("/{paymentId}/success")
    public ResponseEntity<PaymentResponseDTO> markPaymentSuccess(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.markPaymentSuccess(paymentId)
        );
    }

    @PatchMapping("/{paymentId}/failure")
    public ResponseEntity<PaymentResponseDTO> markPaymentFailure(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.markPaymentFailure(paymentId)
        );
    }
}
