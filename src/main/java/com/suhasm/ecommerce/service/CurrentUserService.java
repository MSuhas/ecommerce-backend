package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.entity.User;
import com.suhasm.ecommerce.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrentUserService {

    private final SecurityUtil securityUtil;

    public User getCurrentUser() {

        return securityUtil.getAuthenticatedUser();

    }

}
