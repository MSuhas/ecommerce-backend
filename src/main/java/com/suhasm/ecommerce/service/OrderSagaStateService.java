package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.ReserveStockResponse;
import com.suhasm.ecommerce.dto.StartOrderSagaRequest;
import com.suhasm.ecommerce.entity.Order;
import com.suhasm.ecommerce.entity.OrderSaga;
import com.suhasm.ecommerce.entity.OrderSagaStatus;
import com.suhasm.ecommerce.repository.OrderSagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OrderSagaStateService {

    private final OrderSagaRepository sagaRepository;

    @Transactional
    public OrderSaga createSaga(StartOrderSagaRequest request, String sagaId ,Long userId) {

        OrderSaga orderSaga = OrderSaga.builder()
                .sagaId(sagaId)
                .productId(request.productId())
                .quantity(request.quantity())
                .status(OrderSagaStatus.STARTED)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .userId(userId)
                .build();

        return sagaRepository.save(orderSaga);
    }

    @Transactional
    public void markStockReserved(String sagaId) {

        OrderSaga saga = sagaRepository.findBySagaId(sagaId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Saga not found: " + sagaId));

        saga.setStatus(OrderSagaStatus.STOCK_RESERVED);
        saga.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void markOrderCreated(String sagaId, Long orderId) {

        OrderSaga saga = sagaRepository.findBySagaId(sagaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Saga not found: " + sagaId));

        if (saga.getStatus() != OrderSagaStatus.STOCK_RESERVED) {
            throw new IllegalStateException(
                    "Saga is not ready for order creation. Current status: "
                            + saga.getStatus());
        }

        saga.setOrderId(orderId);
        saga.setStatus(OrderSagaStatus.ORDER_CREATED);
        saga.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void markCompleted(String sagaId) {

        OrderSaga saga = sagaRepository.findBySagaId(sagaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Saga not found: " + sagaId));

        if (saga.getStatus() != OrderSagaStatus.ORDER_CREATED) {
            throw new IllegalStateException(
                    "Saga is not ready to complete. Current status: "
                            + saga.getStatus());
        }

        saga.setStatus(OrderSagaStatus.COMPLETED);
        saga.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void markOrderCreationFailed(String sagaId) {

        OrderSaga saga = sagaRepository.findBySagaId(sagaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Saga not found: " + sagaId));

        if (saga.getStatus() != OrderSagaStatus.STOCK_RESERVED) {
            throw new IllegalStateException(
                    "Saga is not ready for order creation failure. Current status: "
                            + saga.getStatus());
        }

        saga.setStatus(OrderSagaStatus.ORDER_CREATION_FAILED);
        saga.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void markStockReleased(String sagaId) {

        OrderSaga saga = sagaRepository.findBySagaId(sagaId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Saga not found: " + sagaId));

        if (saga.getStatus() != OrderSagaStatus.ORDER_CREATION_FAILED) {
            throw new IllegalStateException(
                    "Saga is not ready for stock release. Current status: "
                            + saga.getStatus());
        }

        saga.setStatus(OrderSagaStatus.STOCK_RELEASED);
        saga.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void markCompensated(String sagaId) {

        OrderSaga saga = sagaRepository.findBySagaId(sagaId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Saga not found: " + sagaId));

        if (saga.getStatus() != OrderSagaStatus.STOCK_RELEASED) {
            throw new IllegalStateException(
                    "Saga is not ready for compensation completion. Current status: "
                            + saga.getStatus());
        }

        saga.setStatus(OrderSagaStatus.COMPENSATED);
        saga.setUpdatedAt(Instant.now());
    }

    @Transactional(readOnly = true)
    public Long getUserId(String sagaId) {

        OrderSaga saga = sagaRepository.findBySagaId(sagaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Saga not found: " + sagaId));

        return saga.getUserId();
    }
}
