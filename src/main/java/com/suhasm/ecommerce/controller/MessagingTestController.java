package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.messaging.OrderCreatedEvent;
import com.suhasm.ecommerce.messaging.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/messaging")
@RequiredArgsConstructor
public class MessagingTestController {

    private final OrderEventPublisher publisher;

    @PostMapping("/test")
    public String test() {

        String eventId = "event_124";/*UUID.randomUUID().toString();*/

        OrderCreatedEvent event =
                new OrderCreatedEvent(
                        101L,
                        5L,
                        eventId,
                        new BigDecimal("1500.00")
                );

        publisher.publish(event);

        return "Message published";
    }
}