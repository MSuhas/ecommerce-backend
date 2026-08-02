package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.UserResponseDTO;
import com.suhasm.ecommerce.entity.User;
import com.suhasm.ecommerce.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final CurrentUserService currentUserService;
    private final UserMapper userMapper;

    public UserResponseDTO getCurrentUser() {

        User user = currentUserService.getCurrentUser();
       return userMapper.toResponse(user);
    }
}
