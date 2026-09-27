package com.suhasm.ecommerce.messaging.kafka;

import com.suhasm.ecommerce.config.KafkaConfig;
import com.suhasm.ecommerce.messaging.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaDltReplayConsumer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    private static final String TARGET_EVENT_ID =
            "042e89d4-782a-40c2-beb4-caa7ec81b594";

   /* @KafkaListener(
            topics = "order-events.DLT",
            groupId = "order-events-dlt-replay",
            properties = "auto.offset.reset:earliest"
    )*/
    public void replay(OrderCreatedEvent event) {

        if (!TARGET_EVENT_ID.equals(event.getEventId())) {
            log.info(
                    "Skipping DLT eventId={}",
                    event.getEventId()
            );
            return;
        }

        log.info(
                "REPLAYING DLT eventId={}, orderId={}",
                event.getEventId(),
                event.getOrderId()
        );

        kafkaTemplate.send(
                KafkaConfig.ORDER_TOPIC,
                String.valueOf(event.getOrderId()),
                event
        );
    }
}
