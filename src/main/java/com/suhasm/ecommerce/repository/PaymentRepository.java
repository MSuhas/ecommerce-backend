package com.suhasm.ecommerce.repository;

import com.suhasm.ecommerce.entity.Order;
import com.suhasm.ecommerce.entity.Payment;
import com.suhasm.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment>findByOrder(Order order);

    Optional<Payment>findByIdAndOrderUser(Long paymentId, User user);
}
