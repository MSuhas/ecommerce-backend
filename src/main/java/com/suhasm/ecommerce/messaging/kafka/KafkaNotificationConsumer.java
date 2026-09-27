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
public class KafkaNotificationConsumer {

    private final KafkaProcessedMessageRepository repository;

    @KafkaListener(
            topics = KafkaConfig.ORDER_TOPIC,
            groupId = KafkaConfig.ORDER_GROUP_NOTIFICATION
    )
    @Transactional
    public void consume(OrderCreatedEvent event) {

        int claimed = repository.claimEvent(
                KafkaConfig.ORDER_GROUP_NOTIFICATION,
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
                "Notification consumer received orderId={}",
                event.getOrderId()
        );
    }
}
