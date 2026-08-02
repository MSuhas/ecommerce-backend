package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.LoginRequest;
import com.suhasm.ecommerce.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        System.out.println("1. Entered login");

        System.out.println(
                passwordEncoder.matches(
                        "password123",
                        "$2a$10$CjkdQTwPPkkQR1.q0t15W.4IbQwzw1QH1Cwt6Eqk3wA6LQxJOgfpi"
                )
        );

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.email(),
                                loginRequest.password()
                        )
                );

        System.out.println("2. Authentication successful");

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        System.out.println("3. Principal extracted");

        String token = jwtService.generateToken(userDetails);

        System.out.println("4. JWT generated");

        return new LoginResponse(token);
    }
}
