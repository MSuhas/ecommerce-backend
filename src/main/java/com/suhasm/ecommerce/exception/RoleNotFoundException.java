package com.suhasm.ecommerce.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String role) {
        super("Role Can not found: " + role);
    }
}
