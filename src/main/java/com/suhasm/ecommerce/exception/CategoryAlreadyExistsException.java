package com.suhasm.ecommerce.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String category) {
        super("Category already exists " + category);
    }
}
