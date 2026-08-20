package com.suhasm.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhasm.ecommerce.dto.PaymentResponseDTO;
import com.suhasm.ecommerce.dto.PaymentWebhookRequest;
import com.suhasm.ecommerce.dto.RazorpayPaymentVerificationRequest;
import com.suhasm.ecommerce.dto.RazorpayWebhookPayload;
import com.suhasm.ecommerce.payment.RazorpayWebhookVerifier;
import com.suhasm.ecommerce.payment.WebhookSignatureVerifier;
import com.suhasm.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
   /* private final WebhookSignatureVerifier webhookSignatureVerifier;*/
    private final ObjectMapper objectMapper;
    private final RazorpayWebhookVerifier razorpayWebhookVerifier;

    @PostMapping("/orders/{orderId}")
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

   /* @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload,
                                              @RequestHeader("X-Webhook-Signature") String signature) {

        if (!webhookSignatureVerifier.verify(payload, signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            PaymentWebhookRequest request =
                    objectMapper.readValue(payload, PaymentWebhookRequest.class);
            paymentService.handleWebhook(request);

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
*/
   @PostMapping("/webhook")
   public ResponseEntity<Void> handleWebhook(
           @RequestBody String payload,
           @RequestHeader("X-Razorpay-Signature") String signature,
           @RequestHeader("x-razorpay-event-id") String eventId) {

       log.info("===== RAZORPAY WEBHOOK RECEIVED =====");
       log.info("Event ID: {}", eventId);

       if (!razorpayWebhookVerifier.verify(payload, signature)) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       }

       try {
           RazorpayWebhookPayload webhookPayload =
                   objectMapper.readValue(
                           payload,
                           RazorpayWebhookPayload.class
                   );

           paymentService.handleRazorpayWebhook(webhookPayload, eventId);

           return ResponseEntity.ok().build();

       } catch (JsonProcessingException e) {
           return ResponseEntity.badRequest().build();
       }
   }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(
            @RequestBody RazorpayPaymentVerificationRequest request) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(request)
        );
    }
}
