package com.suhasm.ecommerce.exception;

public class InvalidPaymentOrderStatusException
        extends RuntimeException {

    public InvalidPaymentOrderStatusException() {
        super("Payment cannot be created for the current order status");
    }
}
