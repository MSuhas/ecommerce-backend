package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.AddCartItemRequestDTO;
import com.suhasm.ecommerce.dto.CartResponseDTO;
import com.suhasm.ecommerce.dto.UpdateCartItemRequestDTO;
import com.suhasm.ecommerce.entity.Cart;
import com.suhasm.ecommerce.entity.CartItem;
import com.suhasm.ecommerce.entity.Product;
import com.suhasm.ecommerce.entity.User;
import com.suhasm.ecommerce.exception.CartNotFoundException;
import com.suhasm.ecommerce.exception.InsufficientStockException;
import com.suhasm.ecommerce.exception.ProductNotFoundException;
import com.suhasm.ecommerce.mapper.CartMapper;
import com.suhasm.ecommerce.repository.CartItemRepository;
import com.suhasm.ecommerce.repository.CartRepository;
import com.suhasm.ecommerce.repository.ProductRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

        Cart cart = getOrCreateCart(user);

        addCartItem(request, cart);

        return buildCartResponse(cart);
    }

    @Transactional(readOnly = true)
    public CartResponseDTO getCart() {

        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user).orElse(null);

        if (null == cart) {
            return new CartResponseDTO(
                    null,
                    List.of(),
                    BigDecimal.ZERO
            );
        }

        return buildCartResponse(cart);
    }

    public CartResponseDTO updateQuantity(
            Long productId,
            UpdateCartItemRequestDTO request) {

        User user = currentUserService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(CartNotFoundException::new);

        AddCartItemRequestDTO cartItemRequest = new AddCartItemRequestDTO(productId, request.getQuantity());
        addCartItem(cartItemRequest, cart);

        return buildCartResponse(cart);
    }


    public CartResponseDTO removeItem(Long productId) {

        User user = currentUserService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user).orElseThrow(CartNotFoundException::new);
        Product product = getProduct(productId);

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(CartNotFoundException::new);

        cart.removeCartItem(cartItem);

        return buildCartResponse(cart);
    }
    public CartResponseDTO clearCart() {
        User user = currentUserService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user).orElseThrow(CartNotFoundException::new);

        cart.clearCart();

        return buildCartResponse(cart);
    }

    @Nonnull
    private CartResponseDTO buildCartResponse(Cart cart) {
        CartResponseDTO cartResponse = cartMapper.toResponse(cart);
        populateTotals(cartResponse);

        return cartResponse;
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .user(user)
                    .build();

            return cartRepository.save(newCart);
        });
    }

    private void addCartItem(AddCartItemRequestDTO request, Cart cart) {
        Product product = getProduct(request.getProductId());

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        if (null != cartItem) {
            Integer quantity = cartItem.getQuantity() + request.getQuantity();
            checkAndUpdateQuantity(cartItem, quantity, product.getStock());
        } else {
           CartItem newCartItem =  CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .build();
            checkAndUpdateQuantity(newCartItem, request.getQuantity(), product.getStock());
            cart.getCartItems().add(newCartItem);
        }
    }

    @Nonnull
    private Product getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return product;
    }

    private void populateTotals(CartResponseDTO cartResponse) {
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
