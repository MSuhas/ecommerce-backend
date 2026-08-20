package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.entity.Payment;
import com.suhasm.ecommerce.entity.PaymentStatus;
import com.suhasm.ecommerce.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

/*@SpringBootTest
public class PaymentRepositoryLockTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void shouldLockPaymentRow() throws ExecutionException, InterruptedException {

        Payment payment = paymentRepository
                .findAll()
                .stream()
                .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                .findFirst()
                .orElseThrow();

        String providerPaymentId = payment.getProviderPaymentId();
        Long orderId = payment.getOrder().getId();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> first = executor.submit(
                () -> lockPayment(providerPaymentId)
        );

        sleep(500); // give A time to acquire the lock

        long start = System.currentTimeMillis();

        Future<?> second = executor.submit(
                () -> lockPayment(providerPaymentId)
        );

        second.get();

        long elapsed = System.currentTimeMillis() - start;

        System.out.println("Transaction B waited: " + elapsed + " ms");

        first.get();
        executor.shutdown();

    }
    private void lockPayment(String providerPaymentId) {

        new TransactionTemplate(transactionManager)
                .executeWithoutResult(status -> {

                    paymentRepository
                            .findByProviderPaymentIdForUpdate(providerPaymentId)
                            .orElseThrow();

                    // Keep this transaction open for now.
                        sleep(3000);
                });
    }
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}*/
