package com.suhasm.ecommerce.exception;

public class InvalidPaymentSignatureException
        extends RuntimeException {

    public InvalidPaymentSignatureException() {
        super("Invalid Razorpay payment signature");
    }
}
