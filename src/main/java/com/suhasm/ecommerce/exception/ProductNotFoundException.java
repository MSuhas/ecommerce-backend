package com.suhasm.ecommerce.exception;

import java.math.BigDecimal;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id){
        System.out.println("Product not found with id " + id);
    }

    public ProductNotFoundException(String name){
        System.out.println("Product not found with name " + name);
    }

    public ProductNotFoundException(String name, BigDecimal price){
        System.out.println("Product not found with name " + name + " price" + price);
    }
}
