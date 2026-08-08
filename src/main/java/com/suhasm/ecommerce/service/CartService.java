package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.AddCartItemRequestDTO;
import com.suhasm.ecommerce.dto.CartResponseDTO;
import com.suhasm.ecommerce.entity.Cart;
import com.suhasm.ecommerce.entity.CartItem;
import com.suhasm.ecommerce.entity.Product;
import com.suhasm.ecommerce.entity.User;
import com.suhasm.ecommerce.exception.InsufficientStockException;
import com.suhasm.ecommerce.exception.ProductNotFoundException;
import com.suhasm.ecommerce.mapper.CartMapper;
import com.suhasm.ecommerce.repository.CartItemRepository;
import com.suhasm.ecommerce.repository.CartRepository;
import com.suhasm.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;
    private final CartMapper cartMapper;

    public CartResponseDTO addItem(AddCartItemRequestDTO request) {
        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .user(user)
                    .build();
            return cartRepository.save(newCart);
        });

        addCartItem(request, cart);

        CartResponseDTO cartResponse = cartMapper.toResponse(cart);

        buildCartResponse(cartResponse);

        return cartResponse;
    }

    private void addCartItem(AddCartItemRequestDTO request, Cart cart) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        boolean[] isNewItem = {false};
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    isNewItem[0] = true;
                    return CartItem.builder()
                            .cart(cart)
                            .product(product)
                            .quantity(0)
                            .build();
                });

        Integer quantity = cartItem.getQuantity() + request.getQuantity();
        checkAndUpdateQuantity(cartItem, quantity, product.getStock());
        if (isNewItem[0]) {
            cart.getCartItems().add(cartItem);
        }
    }

    private void buildCartResponse(CartResponseDTO cartResponse) {
        BigDecimal total = cartResponse.getItems().stream().map(item ->  {
           BigDecimal subTotal =  item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
           item.setSubtotal(subTotal);
           return subTotal;
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        cartResponse.setTotalAmount(total);
    }

    private void checkAndUpdateQuantity(CartItem cartItem, Integer quantity, Integer stock) {

        if (quantity > stock) {
            throw new InsufficientStockException();
        } else {
            cartItem.setQuantity(quantity);
        }
    }
}
