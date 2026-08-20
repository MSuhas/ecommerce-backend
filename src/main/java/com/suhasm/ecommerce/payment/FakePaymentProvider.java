package com.suhasm.ecommerce.payment;

import com.suhasm.ecommerce.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("fakePaymentProvider")
public class FakePaymentProvider implements PaymentProvider{
    @Override
    public PaymentInitiationResult initiatePayment(Payment payment) {
        String providerPaymentId =
                "FAKE-" + UUID.randomUUID();

        String checkoutUrl =
                "http://localhost:8080/fake-payment/"
                        + providerPaymentId;

        return PaymentInitiationResult.builder()
    /*            .providerPaymentId(providerPaymentId)
                .checkoutUrl(checkoutUrl)*/
                .build();
    }
}
