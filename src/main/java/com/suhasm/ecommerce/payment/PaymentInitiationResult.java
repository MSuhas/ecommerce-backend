package com.suhasm.ecommerce.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentInitiationResult {

    private String providerOrderId;
}
