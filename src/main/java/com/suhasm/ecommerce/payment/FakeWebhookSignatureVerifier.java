package com.suhasm.ecommerce.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HexFormat;


@Component
@Slf4j
public class FakeWebhookSignatureVerifier
        implements WebhookSignatureVerifier {

    private final String secret;

    public FakeWebhookSignatureVerifier(
            @Value("${payment.webhook.secret}") String secret) {
        this.secret = secret;
    }

    @Override
    public boolean verify(String payload, String signature) {

        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            secret.getBytes(StandardCharsets.UTF_8),
                            "HmacSHA256"
                    );

            mac.init(secretKey);

            byte[] hash = mac.doFinal(
                    payload.getBytes(StandardCharsets.UTF_8)
            );

            String expectedSignature =
                    HexFormat.of().formatHex(hash);

            log.info("verifying.......");

            log.info("expected sig {}", expectedSignature);
            log.info("actual sig {}", signature);

            return MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.UTF_8),
                    signature.getBytes(StandardCharsets.UTF_8)
            );

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unable to verify webhook signature", e
            );
        }
    }

    public String generateSignature(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            secret.getBytes(StandardCharsets.UTF_8),
                            "HmacSHA256"
                    );

            mac.init(secretKey);

            byte[] hash = mac.doFinal(
                    payload.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unable to generate webhook signature", e);
        }
    }
}