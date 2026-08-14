package com.suhasm.ecommerce.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Cart is Empty");
    }
}
