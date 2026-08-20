package com.suhasm.ecommerce.service;


import com.suhasm.ecommerce.dto.PaymentWebhookRequest;
import com.suhasm.ecommerce.entity.Order;
import com.suhasm.ecommerce.entity.OrderStatus;
import com.suhasm.ecommerce.entity.Payment;
import com.suhasm.ecommerce.entity.PaymentStatus;
import com.suhasm.ecommerce.repository.OrderRepository;
import com.suhasm.ecommerce.repository.PaymentRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*@SpringBootTest
class PaymentServiceConcurrencyTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @SneakyThrows
    @Test
    void shouldProcessSameWebhookSafelyWhenRequestsArriveConcurrently()  {

        Payment payment = paymentRepository
                .findAll()
                .stream()
                .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                .findFirst()
                .orElseThrow();

        String providerPaymentId = payment.getProviderPaymentId();
        Long orderId = payment.getOrder().getId();

        PaymentWebhookRequest request = new PaymentWebhookRequest();
        request.setProviderPaymentId(providerPaymentId);
        request.setStatus("SUCCESS");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> future1 = executor.submit(
                () -> processWebhookInTransaction(request)
        );

        Future<?> future2 = executor.submit(
                () -> processWebhookInTransaction(request)
        );

        future1.get();
        future2.get();

        executor.shutdown();

        Payment updatedPayment = paymentRepository
                .findByProviderPaymentId(providerPaymentId)
                .orElseThrow();

        assertEquals(PaymentStatus.SUCCESS, updatedPayment.getStatus());

        Order updatedOrder = orderRepository
                .findById(orderId)
                .orElseThrow();

        assertEquals(
                OrderStatus.CONFIRMED,
                updatedOrder.getStatus()
        );

    }

    private void processWebhookInTransaction(
            PaymentWebhookRequest request) {

        new TransactionTemplate(transactionManager)
                .executeWithoutResult(status ->
                        paymentService.handleWebhook(request)
                );
    }

}*/