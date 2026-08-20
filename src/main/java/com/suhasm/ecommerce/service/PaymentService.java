package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.PaymentResponseDTO;
import com.suhasm.ecommerce.dto.PaymentWebhookRequest;
import com.suhasm.ecommerce.dto.RazorpayPaymentVerificationRequest;
import com.suhasm.ecommerce.dto.RazorpayWebhookPayload;
import com.suhasm.ecommerce.entity.*;
import com.suhasm.ecommerce.exception.*;
import com.suhasm.ecommerce.mapper.PaymentMapper;
import com.suhasm.ecommerce.payment.PaymentInitiationResult;
import com.suhasm.ecommerce.payment.PaymentProvider;
import com.suhasm.ecommerce.payment.RazorpayPaymentVerifier;
import com.suhasm.ecommerce.repository.OrderRepository;
import com.suhasm.ecommerce.repository.PaymentRepository;
import com.suhasm.ecommerce.repository.PaymentWebhookEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.suhasm.ecommerce.entity.PaymentStatus.*;

@Service
@Transactional
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CurrentUserService currentUserService;
    private final PaymentMapper paymentMapper;
    private final PaymentProvider paymentProvider;
    private final RazorpayPaymentVerifier razorpayPaymentVerifier;
    private final PaymentWebhookEventRepository webhookEventRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            CurrentUserService currentUserService,
            PaymentMapper paymentMapper,
            @Qualifier("razorpayPaymentProvider")
            PaymentProvider paymentProvider,
            RazorpayPaymentVerifier razorpayPaymentVerifier,
            PaymentWebhookEventRepository webhookEventRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.currentUserService = currentUserService;
        this.paymentMapper = paymentMapper;
        this.paymentProvider = paymentProvider;
        this.razorpayPaymentVerifier = razorpayPaymentVerifier;
        this.webhookEventRepository = webhookEventRepository;
    }

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

        Payment savedPayment = paymentRepository.save(payment);

        PaymentInitiationResult paymentInitiationResult = paymentProvider.initiatePayment(savedPayment);

        savedPayment.setProviderOrderId(paymentInitiationResult.getProviderOrderId());

        PaymentResponseDTO responseDTO =  paymentMapper.toResponse(savedPayment);

        responseDTO.setProviderOrderId(paymentInitiationResult.getProviderOrderId());

        return responseDTO;
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

    public void handleWebhook(PaymentWebhookRequest webhookRequest) {

        log.info("handleWebhook {}", webhookRequest.getProviderPaymentId());

        Payment payment = paymentRepository
                .findByProviderPaymentIdForUpdate(
                        webhookRequest.getProviderPaymentId()
                )
                .orElseThrow(PaymentNotFoundException::new);

        // Duplicate SUCCESS webhook
        if (payment.getStatus() == SUCCESS
                && "SUCCESS".equalsIgnoreCase(webhookRequest.getStatus())) {
            return;
        }

        // Duplicate FAILED webhook
        if (payment.getStatus() == FAILED
                && "FAILED".equalsIgnoreCase(webhookRequest.getStatus())) {
            return;
        }

        if (payment.getStatus() != PENDING) {
            throw new InvalidPaymentStatusException();
        }

        if ("SUCCESS".equalsIgnoreCase(webhookRequest.getStatus())) {

            payment.setStatus(SUCCESS);

            Order order = payment.getOrder();
            order.setStatus(OrderStatus.CONFIRMED);

        } else if ("FAILED".equalsIgnoreCase(webhookRequest.getStatus())) {

            payment.setStatus(FAILED);

        } else {
            throw new InvalidPaymentStatusException();
        }
    }

    public PaymentResponseDTO verifyPayment(
            RazorpayPaymentVerificationRequest request){

        Payment payment = paymentRepository.findByProviderOrderId(request.getRazorpayOrderId())
                .orElseThrow(PaymentNotFoundException::new);

        boolean valid = razorpayPaymentVerifier.verify(
                payment.getProviderOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        if (!valid) {
            throw new InvalidPaymentSignatureException();
        }

        if (payment.getStatus() == SUCCESS) {
            return paymentMapper.toResponse(payment);
        }

        if (payment.getStatus() != PENDING) {
            throw new InvalidPaymentStatusException();
        }

        payment.setProviderPaymentId(
                request.getRazorpayPaymentId()
        );

        payment.setStatus(SUCCESS);

        payment.getOrder().setStatus(OrderStatus.CONFIRMED);

        return paymentMapper.toResponse(payment);
    }

    public void handleRazorpayWebhook(
            RazorpayWebhookPayload webhookPayload,
            String eventId) {


        if (webhookEventRepository.existsByEventId(eventId)) {
            return;
        }

        if (!"payment.captured".equals(webhookPayload.getEvent())) {
            return;
        }

        RazorpayWebhookPayload.PaymentEntity paymentEntity =
                webhookPayload
                        .getPayload()
                        .getPayment()
                        .getEntity();

        String providerOrderId = paymentEntity.getOrder_id();
        String providerPaymentId = paymentEntity.getId();

        Payment payment = paymentRepository
                .findByProviderOrderIdForUpdate(providerOrderId)
                .orElseThrow(PaymentNotFoundException::new);

        if (payment.getStatus() == SUCCESS) {
            return;
        }

        if (payment.getStatus() != PENDING) {
            throw new InvalidPaymentStatusException();
        }

        payment.setProviderPaymentId(providerPaymentId);
        payment.setStatus(SUCCESS);

        payment.getOrder()
                .setStatus(OrderStatus.CONFIRMED);

        PaymentWebhookEvent event = PaymentWebhookEvent.builder()
                .eventId(eventId)
                .provider("RAZORPAY")
                .build();

        webhookEventRepository.save(event);
    }
}
