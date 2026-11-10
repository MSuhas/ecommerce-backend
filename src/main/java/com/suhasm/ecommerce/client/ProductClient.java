package com.suhasm.ecommerce.client;

import com.suhasm.ecommerce.dto.ReleaseStockRequest;
import com.suhasm.ecommerce.dto.ReleaseStockResponse;
import com.suhasm.ecommerce.dto.ReserveStockRequest;
import com.suhasm.ecommerce.dto.ReserveStockResponse;
import com.suhasm.ecommerce.exception.ProductServiceUnavailableException;
import com.suhasm.ecommerce.exception.ProductStockException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final RestClient productRestClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Retry(name = "productService")
    public ReserveStockResponse reserveStock(
            Long productId,
            int quantity,
            String reservationID) {

        log.info(">>> ProductClient.reserveStock CALLED: productId={}, quantity={}", productId, quantity);

        var circuitBreaker =  circuitBreakerFactory
                .create("productService");
        CircuitBreaker circuitBreakerReg =
                circuitBreakerRegistry.circuitBreaker("productService");
        circuitBreakerReg.getEventPublisher()
                .onStateTransition(event ->
                        log.warn(
                                ">>> CIRCUIT STATE: {}",
                                event.getStateTransition()
                        ));

                return  circuitBreaker.run( ()->   {  log.info(">>> CIRCUIT BREAKER: executing HTTP call");
                return getReserveStockResponse(productId, quantity, reservationID); },
        throwable -> {
            log.error(">>> CIRCUIT BREAKER FALLBACK: {} - {}", throwable.getClass().getName(), throwable.getMessage());
            throw new ProductServiceUnavailableException(
                    "Product Service is currently unavailable",
                    throwable
            );
        });
    }

    @Nullable
    private ReserveStockResponse getReserveStockResponse(Long productId, int quantity, String reservationId) {
        try {
            return productRestClient
                    .post()
                    .uri("/api/products/{id}/reserve-stock", productId)
                    .body(new ReserveStockRequest(quantity, reservationId))
                    .retrieve()
                    .onStatus(httpStatusCode -> httpStatusCode.value() == 409,
                            (request, response) -> {
                                throw new ProductStockException(
                                        "Insufficient stock for product " + productId
                                );
                            })
                    .body(ReserveStockResponse.class);

        } catch (ProductStockException ex) {
            throw ex;

        } catch (RestClientException ex) {
            throw new ProductServiceUnavailableException(
                    "Product Service is currently unavailable",
                    ex
            );
        }
    }

    public ReleaseStockResponse releaseStock(
            Long productId,
            int quantity,
            String reservationId) {

        try {
            return productRestClient.post()
                    .uri("/api/products/{id}/release-stock", productId)
                    .body(new ReleaseStockRequest(quantity, reservationId))
                    .retrieve()
                    .body(ReleaseStockResponse.class);

        } catch (RestClientException ex) {
            throw new ProductServiceUnavailableException(
                    "Product Service is currently unavailable", ex);
        }
    }

}
