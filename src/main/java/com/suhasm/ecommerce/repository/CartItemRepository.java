package com.suhasm.ecommerce.repository;

import com.suhasm.ecommerce.entity.Cart;
import com.suhasm.ecommerce.entity.CartItem;
import com.suhasm.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository <CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
