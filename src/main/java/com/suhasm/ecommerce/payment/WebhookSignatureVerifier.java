package com.suhasm.ecommerce.payment;

public interface WebhookSignatureVerifier {

    boolean verify(String payload, String signature);
}