package com.suhasm.ecommerce.payment;

import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RazorpayPaymentVerifier {

    private final String keySecret;

    public RazorpayPaymentVerifier(
            @Value("${razorpay.key-secret}") String keySecret) {
        this.keySecret = keySecret;
    }

    public boolean verify(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        try {
            JSONObject options = new JSONObject();

            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            return Utils.verifyPaymentSignature(
                    options,
                    keySecret
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to verify Razorpay payment signature",
                    e
            );
        }
    }
}