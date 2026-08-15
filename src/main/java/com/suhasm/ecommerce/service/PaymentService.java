package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.PaymentResponseDTO;
import com.suhasm.ecommerce.entity.Order;
import com.suhasm.ecommerce.entity.OrderStatus;
import com.suhasm.ecommerce.entity.Payment;
import com.suhasm.ecommerce.entity.User;
import com.suhasm.ecommerce.exception.*;
import com.suhasm.ecommerce.mapper.PaymentMapper;
import com.suhasm.ecommerce.repository.OrderRepository;
import com.suhasm.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.suhasm.ecommerce.entity.PaymentStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CurrentUserService currentUserService;
    private final PaymentMapper paymentMapper;

    public PaymentResponseDTO createPayment(Long orderId) {

        User user = currentUserService.getCurrentUser();
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(OrderNotFoundException::new);

        if(paymentRepository.findByOrder(order).isPresent()) {
            throw new PaymentAlreadyExistsException();
        }

        if(order.getStatus() != OrderStatus.PENDING) {
            throw  new InvalidPaymentOrderStatusException();
        }

        Payment payment = Payment.builder()
                .order(order)
                .status(PENDING)
                .amount(order.getTotalAmount())
                .build();

        paymentRepository.save(payment);

        return paymentMapper.toResponse(payment);
    }

    public PaymentResponseDTO markPaymentSuccess(Long paymentId) {

        User user = currentUserService.getCurrentUser();
        Payment payment = paymentRepository.findByIdAndOrderUser(paymentId, user)
                .orElseThrow(PaymentNotFoundException::new);
        if (payment.getStatus() != PENDING) {
            throw new InvalidPaymentStatusException();
        }
        payment.setStatus(SUCCESS);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CONFIRMED);

        return paymentMapper.toResponse(payment);
    }

    public PaymentResponseDTO markPaymentFailure(Long paymentId) {
        User user = currentUserService.getCurrentUser();
        Payment payment = paymentRepository.findByIdAndOrderUser(paymentId, user)
                .orElseThrow(PaymentNotFoundException::new);
        if (payment.getStatus() != PENDING) {
            throw new InvalidPaymentStatusException();
        }
        payment.setStatus(FAILED);

        return paymentMapper.toResponse(payment);
    }
}
