package com.suhasm.ecommerce.exception;


public class AuthenticatedUserNotFoundException extends RuntimeException {

    public AuthenticatedUserNotFoundException(String email) {
        super("User Not Authenticated " + email);
    }
}
