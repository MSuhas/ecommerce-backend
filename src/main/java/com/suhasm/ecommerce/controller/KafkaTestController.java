package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.messaging.OrderCreatedEvent;
import com.suhasm.ecommerce.messaging.kafka.KafkaOrderEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/messaging/kafka")
@RequiredArgsConstructor
public class KafkaTestController {
    private final KafkaOrderEventPublisher publisher;

    @PostMapping("/test")
    public ResponseEntity<String> publishTestEvent() {

        OrderCreatedEvent event = new OrderCreatedEvent(
                999999L,
                999L,
                UUID.randomUUID().toString(),
                new BigDecimal("1499.00")
        );

        publisher.publish(event);

        return ResponseEntity.ok("Kafka event published");
    }
}
