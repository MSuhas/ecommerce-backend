package com.suhasm.ecommerce.exception;

public class InvalidOrderStatusTransitionException
        extends RuntimeException {

    public InvalidOrderStatusTransitionException() {
        super("Invalid order status transition");
    }
}