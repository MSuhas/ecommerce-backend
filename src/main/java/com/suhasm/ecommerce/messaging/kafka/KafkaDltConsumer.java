package com.suhasm.ecommerce.messaging.kafka;

import com.suhasm.ecommerce.messaging.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaDltConsumer {
    @KafkaListener(
            topics = "order-events.DLT",
            groupId = "order-events-dlt"
    )
    public void consume(OrderCreatedEvent event) {

        log.error(
                "DLT EVENT RECEIVED: eventId={}, orderId={}, userId={}, totalAmount={}",
                event.getEventId(),
                event.getOrderId(),
                event.getUserId(),
                event.getTotalAmount()
        );
    }
}
