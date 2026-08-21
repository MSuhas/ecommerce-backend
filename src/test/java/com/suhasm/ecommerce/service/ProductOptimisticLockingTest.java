package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.entity.Category;
import com.suhasm.ecommerce.entity.Product;
import com.suhasm.ecommerce.repository.CategoryRepository;
import com.suhasm.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductOptimisticLockingTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void shouldDetectConcurrentProductUpdate() throws Exception {

        Category category = categoryRepository.save(
                Category.builder()
                        .name("Optimistic Test Category1")
                        .build()
        );

        Product product = productRepository.save(
                Product.builder()
                        .name("Optimistic Test Product1")
                        .description("Test")
                        .price(new BigDecimal("100"))
                        .stock(10)
                        .version(0L)
                        .category(category)
                        .build()
        );

        Long productId = product.getId();

        CyclicBarrier barrier = new CyclicBarrier(2);

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        Callable<Boolean> updateAttempt = () -> {
            try {
                new TransactionTemplate(transactionManager)
                        .executeWithoutResult(status -> {

                            Product loaded =
                                    productRepository
                                            .findById(productId)
                                            .orElseThrow();

                            try {
                                barrier.await();
                            } catch (InterruptedException | BrokenBarrierException e) {
                                throw new RuntimeException(e);
                            }

                            loaded.setStock(
                                    loaded.getStock() - 1
                            );

                            productRepository.saveAndFlush(loaded);
                        });

                return true;

            } catch (OptimisticLockingFailureException e) {
                return false;

            }
        };

        Future<Boolean> future1 =
                executor.submit(updateAttempt);

        Future<Boolean> future2 =
                executor.submit(updateAttempt);

        boolean result1 = future1.get();
        boolean result2 = future2.get();

        executor.shutdown();

        assertTrue(
                result1 ^ result2,
                "Exactly one transaction should succeed"
        );

        Product finalProduct =
                productRepository
                        .findById(productId)
                        .orElseThrow();

        assertEquals(9, finalProduct.getStock());

        assertEquals(
                1L,
                finalProduct.getVersion()
        );

        productRepository.deleteById(productId);
        categoryRepository.deleteById(category.getId());
    }
}