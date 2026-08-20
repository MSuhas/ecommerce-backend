package com.suhasm.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentWebhookRequest {

    private String providerPaymentId;
    private String status;
}