package com.suhasm.ecommerce.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhasm.ecommerce.entity.OutboxEvent;
import com.suhasm.ecommerce.entity.OutboxStatus;
import com.suhasm.ecommerce.repository.OutboxEventRepository;
import com.suhasm.ecommerce.service.OutboxClaimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.Instant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxClaimService outboxClaimService;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    /*Marked with big value for now*/
    @Scheduled(fixedDelay = 500000)
    @Transactional
    public void publishPendingEvents() {

        List<OutboxEvent> events = outboxClaimService.claimPendingEvents();

        for (OutboxEvent event : events) {

            try {
                OrderCreatedEvent orderCreatedEvent =
                        objectMapper.readValue(
                                event.getPayload(),
                                OrderCreatedEvent.class
                        );

                CorrelationData correlationData = new CorrelationData(String.valueOf(event.getId()));
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.ORDER_EXCHANGE,
                        RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                        orderCreatedEvent,
                        correlationData
                );

                CorrelationData.Confirm confirm =
                        correlationData
                                .getFuture()
                                .get(10, TimeUnit.SECONDS);

                if (!confirm.isAck()) {
                    throw new IllegalStateException(
                            "RabbitMQ rejected outbox event: "
                                    + confirm.getReason()
                    );
                }

                event.setPublishedAt(now());
                event.setStatus(OutboxStatus.PUBLISHED);

                outboxEventRepository.save(event);

                log.info(
                        "Published outbox event: id={}, aggregateId={}",
                        event.getId(),
                        event.getAggregateId()
                );

            } catch (JsonProcessingException e) {

                log.error(
                        "Failed to deserialize outbox event: id={}",
                        event.getId(),
                        e
                );
            } catch (Exception e) {

                log.error(
                        "Failed to publish outbox event: id={}",
                        event.getId(),
                        e
                );
            }
        }
    }
}
