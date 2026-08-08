package com.suhasm.ecommerce.repository;

import com.suhasm.ecommerce.entity.Cart;
import com.suhasm.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
