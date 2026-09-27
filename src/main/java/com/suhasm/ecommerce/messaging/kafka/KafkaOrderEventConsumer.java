package com.suhasm.ecommerce.messaging.kafka;

import com.suhasm.ecommerce.config.KafkaConfig;
import com.suhasm.ecommerce.messaging.OrderCreatedEvent;
import com.suhasm.ecommerce.repository.KafkaProcessedMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaOrderEventConsumer {

    private final KafkaProcessedMessageRepository repository;

    @KafkaListener(
            topics = KafkaConfig.ORDER_TOPIC,
            groupId = KafkaConfig.ORDER_GROUP
    )
    @Transactional
    public void consume(OrderCreatedEvent event) {

        log.info(">>> ORDER CONSUMER RECEIVED eventId={}", event.getEventId());

        int claimed = repository.claimEvent(
                KafkaConfig.ORDER_GROUP,
                event.getEventId(),
                Instant.now()
        );

        if (claimed == 0) {
            log.info(
                    "Duplicate Kafka event ignored: {}",
                    event.getEventId()
            );
            return;
        }

        log.info(
                "Received OrderCreatedEvent: eventId={}, orderId={}, userId={}, totalAmount={}",
                event.getEventId(),
                event.getOrderId(),
                event.getUserId(),
                event.getTotalAmount()
        );

    }
}
