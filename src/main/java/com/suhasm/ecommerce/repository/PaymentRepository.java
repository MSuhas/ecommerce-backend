package com.suhasm.ecommerce.repository;

import com.suhasm.ecommerce.entity.Order;
import com.suhasm.ecommerce.entity.Payment;
import com.suhasm.ecommerce.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment>findByOrder (Order order);

    Optional<Payment>findByIdAndOrderUser(Long paymentId, User user);

    Optional<Payment>findByProviderPaymentId(String providerPaymentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       select p
       from Payment p
       where p.id = :paymentId
       """)
    Optional<Payment> findByIdForUpdate(@Param("paymentId") Long paymentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       select p
       from Payment p
       where p.providerPaymentId = :providerPaymentId
       """)
    Optional<Payment> findByProviderPaymentIdForUpdate(
            @Param("providerPaymentId") String providerPaymentId);

    Optional<Payment> findByProviderOrderId(String providerOrderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       select p
       from Payment p
       where p.providerOrderId = :providerOrderId
       """)
    Optional<Payment> findByProviderOrderIdForUpdate(
            @Param("providerOrderId") String providerOrderId);
}
