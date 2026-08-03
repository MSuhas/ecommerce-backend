package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.UserResponseDTO;
import com.suhasm.ecommerce.dto.UserUpdateRequestDTO;
import com.suhasm.ecommerce.entity.User;
import com.suhasm.ecommerce.mapper.UserMapper;
import com.suhasm.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final CurrentUserService currentUserService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserResponseDTO getCurrentUser() {

        User user = currentUserService.getCurrentUser();
       return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO) {

        User user = currentUserService.getCurrentUser();
        user.setFirstName(userUpdateRequestDTO.getFirstName());
        user.setLastName(userUpdateRequestDTO.getLastName());
        user.setPhone(userUpdateRequestDTO.getPhone());

        return userMapper.toResponse(user);
    }
}
