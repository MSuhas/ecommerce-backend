package com.suhasm.ecommerce.exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException() {
        super("Payment not found");
    }
}
