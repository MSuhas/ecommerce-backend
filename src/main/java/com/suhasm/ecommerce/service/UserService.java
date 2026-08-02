package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.UserRequestDTO;
import com.suhasm.ecommerce.dto.UserResponseDTO;
import com.suhasm.ecommerce.entity.Role;
import com.suhasm.ecommerce.entity.User;
import com.suhasm.ecommerce.exception.EmailAlreadyExistsException;
import com.suhasm.ecommerce.exception.RoleNotFoundException;
import com.suhasm.ecommerce.mapper.UserMapper;
import com.suhasm.ecommerce.repository.RoleRepository;
import com.suhasm.ecommerce.repository.UserRepository;
import com.suhasm.ecommerce.util.RoleType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO register(UserRequestDTO userRequestDTO) {

        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(userRequestDTO.getEmail());
        }

        Role role = roleRepository.findByName(RoleType.CUSTOMER.name())
                .orElseThrow(() -> new RoleNotFoundException("CUSTOMER"));

        User user = userMapper.toEntity(userRequestDTO);

        String encryptedPassword = passwordEncoder.encode(userRequestDTO.getPassword());

        user.setPassword(encryptedPassword);
        user.setRole(role);
        user.setActive(true);

        User userEntity= userRepository.save(user);

        return userMapper.toResponse(userEntity);
    }
}
