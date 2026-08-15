package com.suhasm.ecommerce.exception;

public class InvalidPaymentStatusException extends RuntimeException {

    public InvalidPaymentStatusException() {
        super("Invalid payment status transition");
    }
}
