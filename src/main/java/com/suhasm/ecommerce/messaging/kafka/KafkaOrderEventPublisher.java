package com.suhasm.ecommerce.messaging.kafka;

import com.suhasm.ecommerce.config.KafkaConfig;
import com.suhasm.ecommerce.messaging.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaOrderEventPublisher {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publish(OrderCreatedEvent event) {

        kafkaTemplate.send(
                KafkaConfig.ORDER_TOPIC,
                String.valueOf(event.getOrderId()),
                event
        );
    }
}
