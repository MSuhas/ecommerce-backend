package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.UserResponseDTO;
import com.suhasm.ecommerce.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUserProfile() {

       return ResponseEntity.ok(usersService.getCurrentUser());
    }
}
