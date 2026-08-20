package com.suhasm.ecommerce.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.suhasm.ecommerce.entity.Payment;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component("razorpayPaymentProvider")
public class RazorpayPaymentProvider implements PaymentProvider{

    private final RazorpayClient razorpayClient;

    public RazorpayPaymentProvider(
            @Value("${razorpay.key-id}") String keyId,
            @Value("${razorpay.key-secret}") String keySecret) {

        try {
            this.razorpayClient =
                    new RazorpayClient(keyId, keySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentInitiationResult initiatePayment(Payment payment) {
        long amountInPaise = payment.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();
        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put(
                "receipt",
                "ORDER_" + payment.getOrder().getId()
        );

        try {
            Order razorpayOrder =
                    razorpayClient.orders.create(orderRequest);

            return PaymentInitiationResult.builder()
                    .providerOrderId(razorpayOrder.get("id"))
                    .build();

        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }
}
