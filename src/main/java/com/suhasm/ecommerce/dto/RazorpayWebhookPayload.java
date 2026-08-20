package com.suhasm.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RazorpayWebhookPayload {

    private String event;
    private Payload payload;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Payload {

        private PaymentPayload payment;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PaymentPayload {

        private PaymentEntity entity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PaymentEntity {

        private String id;
        private String order_id;
        private String status;
    }
}