package com.suhasm.ecommerce.payment;

import com.suhasm.ecommerce.entity.Payment;

public interface PaymentProvider {

    PaymentInitiationResult initiatePayment(Payment payment);
}
