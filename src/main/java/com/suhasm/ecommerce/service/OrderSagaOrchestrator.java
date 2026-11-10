package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.client.ProductClient;
import com.suhasm.ecommerce.dto.ReserveStockResponse;
import com.suhasm.ecommerce.dto.StartOrderSagaRequest;
import com.suhasm.ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSagaOrchestrator {

    private final ProductClient productClient;
    private final OrderSagaStateService sagaStateService;
    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public String startSaga(StartOrderSagaRequest request) {

        String sagaId = UUID.randomUUID().toString();

        User user = currentUserService.getCurrentUser();

        // Step 1: Persist STARTED in its own transaction.
        sagaStateService.createSaga(request, sagaId, user.getId());

        log.info("Saga STARTED: {}", sagaId);

        // Step 2: Reserve stock in Product Service.
        // This call is outside the order-creation try-catch.
        ReserveStockResponse reservation = productClient.reserveStock(
                request.productId(),
                request.quantity(),
                sagaId
        );

        // Step 3: Record successful stock reservation.
        sagaStateService.markStockReserved(sagaId);

        // Step 4: Create the order.
        // Only failures from this operation trigger compensation.
        final Long orderId;

        try {
            orderId = orderService.createOrderForSaga(
                    reservation,
                    sagaId,
                    sagaStateService.getUserId(sagaId));

        } catch (RuntimeException ex) {

            log.error("Order creation failed: sagaId={}", sagaId, ex);

            compensateFailedOrderCreation(
                    sagaId,
                    request,
                    ex
            );

            throw ex;
        }

        // Step 5: Record order creation.
        sagaStateService.markOrderCreated(sagaId, orderId);

        log.info(
                "Saga ORDER_CREATED: sagaId={}, orderId={}",
                sagaId,
                orderId
        );

        // Step 6: Complete the Saga.
        sagaStateService.markCompleted(sagaId);

        log.info(
                "Saga COMPLETED: sagaId={}, orderId={}",
                sagaId,
                orderId
        );

        return sagaId;
    }

    private void compensateFailedOrderCreation(
            String sagaId,
            StartOrderSagaRequest request,
            RuntimeException originalException) {

        try {
            // Record that order creation failed.
            sagaStateService.markOrderCreationFailed(sagaId);

            // Release the stock reserved by this Saga.
            productClient.releaseStock(
                    request.productId(),
                    request.quantity(),
                    sagaId
            );

            log.info("Stock released: sagaId={}", sagaId);

            // Record successful compensation steps.
            sagaStateService.markStockReleased(sagaId);
            sagaStateService.markCompensated(sagaId);

            log.info("Saga COMPENSATED: sagaId={}", sagaId);

        } catch (RuntimeException compensationException) {

            log.error(
                    "Saga compensation failed: sagaId={}",
                    sagaId,
                    compensationException
            );

            // Keep the original order-creation exception as the primary failure.
            originalException.addSuppressed(compensationException);
        }
    }
}

