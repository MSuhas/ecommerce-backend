package com.suhasm.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhasm.ecommerce.dto.OrderResponseDTO;
import com.suhasm.ecommerce.entity.*;
import com.suhasm.ecommerce.exception.*;
import com.suhasm.ecommerce.mapper.OrderMapper;
import com.suhasm.ecommerce.messaging.OrderCreatedEvent;
import com.suhasm.ecommerce.repository.CartRepository;
import com.suhasm.ecommerce.repository.OrderRepository;
import com.suhasm.ecommerce.repository.OutboxEventRepository;
import com.suhasm.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.suhasm.ecommerce.entity.OrderStatus.*;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;
    private final OrderMapper orderMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public OrderResponseDTO placeOrder() {
        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user).orElseThrow(CartNotFoundException::new);

        if (cart.getCartItems().isEmpty()) {
            throw new EmptyCartException();
        }

        List<CartItem> cartItems = cart.getCartItems();

        final Order order = Order.builder()
                .user(user)
                .status(PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            Long productId = cartItem.getProduct().getId();
            Product product = productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new InsufficientStockException();
            }

            product.setStock(product.getStock() - cartItem.getQuantity());

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .productName(product.getName())
                    .unitPrice(product.getPrice())
                    .quantity(cartItem.getQuantity())
                    .subtotal(subtotal)
                    .build();

            order.getOrderItems().add(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        String eventId = UUID.randomUUID().toString();
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                user.getId(),
                eventId,
                savedOrder.getTotalAmount()
        );

        String payload;

        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Failed to serialize OrderCreatedEvent",
                    e
            );
        }

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .eventType("ORDER_CREATED")
                .aggregateType("ORDER")
                .aggregateId(savedOrder.getId())
                .payload(payload)
                .createdAt(Instant.now())
                .status(OutboxStatus.PENDING)
                .build();

        outboxEventRepository.save(outboxEvent);

        cartItems.clear();

        return orderMapper.toResponse(savedOrder);
    }

    public Page<OrderResponseDTO> findOrders(Pageable pageable) {

        User user = currentUserService.getCurrentUser();
        Page<Order> orders = orderRepository.findByUser(user, pageable);
        return orders.map(orderMapper::toResponse);
    }

    public OrderResponseDTO findOrder(Long orderId) {
        User user = currentUserService.getCurrentUser();
        Order order = orderRepository.findByIdAndUser(orderId, user).orElseThrow(OrderNotFoundException::new);

        return orderMapper.toResponse(order);
    }

    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newOrderStatus) {

        User user = currentUserService.getCurrentUser();
        Order order = orderRepository.findByIdAndUser(orderId, user).orElseThrow(OrderNotFoundException::new);

        if (!order.getStatus().canTransitionTo(newOrderStatus)) {
            throw new InvalidOrderStatusTransitionException();
        }

        order.setStatus(newOrderStatus);

        return orderMapper.toResponse(order);
    }

    public OrderResponseDTO cancelOrder(Long orderId) {
        User user = currentUserService.getCurrentUser();
        Order order = orderRepository.findByIdAndUser(orderId, user).orElseThrow(OrderNotFoundException::new);

        if (!order.getStatus().canTransitionTo(CANCELLED)) {
            throw new InvalidOrderStatusTransitionException();
        }

        for (OrderItem orderItem: order.getOrderItems()) {

            Product product = productRepository.findByIdForUpdate(orderItem.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(orderItem.getProductId()));
            product.setStock(product.getStock() + orderItem.getQuantity());
        }

        order.setStatus(CANCELLED);

        return orderMapper.toResponse(order);
    }
}
