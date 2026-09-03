package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.entity.ProcessedMessage;
import com.suhasm.ecommerce.repository.ProcessedMessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ProcessedMessageConcurrencyTest {

    @Autowired
    private ProcessedMessageRepository processedMessageRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private static final String EVENT_ID = "concurrent-event-123";

    @AfterEach
    void cleanup() {
        processedMessageRepository
                .findAll()
                .stream()
                .filter(message -> EVENT_ID.equals(message.getEventId()))
                .forEach(processedMessageRepository::delete);
    }

    @Test
    void shouldAllowOnlyOneInsertForSameEventId() throws Exception {

        CyclicBarrier barrier = new CyclicBarrier(2);

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        Callable<Boolean> insertAttempt = () -> {
            try {
                new TransactionTemplate(transactionManager)
                        .executeWithoutResult(status -> {

                            try {
                                barrier.await();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                throw new RuntimeException(e);
                            } catch (BrokenBarrierException e) {
                                throw new RuntimeException(e);
                            }

                            processedMessageRepository.saveAndFlush(
                                    new ProcessedMessage(EVENT_ID)
                            );
                        });

                return true;

            } catch (DataIntegrityViolationException e) {
                return false;
            }
        };

        Future<Boolean> future1 =
                executor.submit(insertAttempt);

        Future<Boolean> future2 =
                executor.submit(insertAttempt);

        boolean result1 = future1.get();
        boolean result2 = future2.get();

        executor.shutdown();

        assertTrue(
                result1 ^ result2,
                "Exactly one insert should succeed"
        );

        long count =
                processedMessageRepository.findAll()
                        .stream()
                        .filter(message ->
                                EVENT_ID.equals(message.getEventId()))
                        .count();

        assertEquals(1, count);
    }
}