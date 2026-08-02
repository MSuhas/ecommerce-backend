package com.suhasm.ecommerce.mapper;

import com.suhasm.ecommerce.dto.UserRequestDTO;
import com.suhasm.ecommerce.dto.UserResponseDTO;
import com.suhasm.ecommerce.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {

        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        return user;
    }

    public UserResponseDTO toResponse(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setActive(user.isActive());
        dto.setRole(user.getRole().getName());

        return dto;
    }
}
