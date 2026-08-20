package com.suhasm.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RazorpayPaymentVerificationRequest {

    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
