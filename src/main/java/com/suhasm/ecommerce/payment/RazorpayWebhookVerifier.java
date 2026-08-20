package com.suhasm.ecommerce.payment;

import com.razorpay.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RazorpayWebhookVerifier {

    private final String webhookSecret;

    public RazorpayWebhookVerifier(
            @Value("${razorpay.webhook-secret}") String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    public boolean verify(String payload, String signature) {

        try {
            return Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    webhookSecret
            );
        } catch (Exception e) {
            return false;
        }
    }
}
