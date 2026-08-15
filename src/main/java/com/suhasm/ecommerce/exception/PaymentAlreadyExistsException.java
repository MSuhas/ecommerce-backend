package com.suhasm.ecommerce.exception;

public class PaymentAlreadyExistsException extends RuntimeException {

    public PaymentAlreadyExistsException() {
        super("Payment already exists for this order");
    }
}