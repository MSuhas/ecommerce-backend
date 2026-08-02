package com.suhasm.ecommerce.util;

import com.suhasm.ecommerce.entity.User;
import com.suhasm.ecommerce.exception.AuthenticatedUserNotFoundException;
import com.suhasm.ecommerce.repository.UserRepository;
import com.suhasm.ecommerce.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {

            throw new AuthenticationCredentialsNotFoundException(
                    "Authenticated user not found.");
        }

        String email = userDetails.getUsername();
       return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticatedUserNotFoundException(email));
    }

}
