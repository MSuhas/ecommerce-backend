package com.suhasm.ecommerce.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException() {
        super("Cart not found ");
    }
}
