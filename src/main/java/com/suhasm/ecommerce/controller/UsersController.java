package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.UserResponseDTO;
import com.suhasm.ecommerce.dto.UserUpdateRequestDTO;
import com.suhasm.ecommerce.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUserProfile() {

       return ResponseEntity.ok(usersService.getCurrentUser());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateUser(
            @RequestBody @Valid UserUpdateRequestDTO userUpdateRequestDTO) {
        return ResponseEntity.ok(usersService.updateUser(userUpdateRequestDTO));
    }
}
