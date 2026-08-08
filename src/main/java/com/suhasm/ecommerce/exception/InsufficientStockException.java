package com.suhasm.ecommerce.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException() {
        super("Product out of stock");
    }
}
