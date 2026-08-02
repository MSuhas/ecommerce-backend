package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.LoginRequest;
import com.suhasm.ecommerce.dto.LoginResponse;
import com.suhasm.ecommerce.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return authenticationService.login(request);
    }
}
