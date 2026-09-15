package com.suhasm.ecommerce.messaging;

import com.suhasm.ecommerce.config.RabbitMQConfig;
import com.suhasm.ecommerce.repository.ProcessedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final ProcessedMessageRepository processedMessageRepository;

   @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE, containerFactory = "rabbitListenerContainerFactory")
   @Transactional
   public void consume(OrderCreatedEvent event) {

       log.info(
               "Processing eventId={}, orderId={}",
               event.getEventId(),
               event.getOrderId()
       );

       int claimed = processedMessageRepository.claimEvent(
               event.getEventId(),
               Instant.now()
       );

       if (claimed == 0) {
           log.info(
                   "Duplicate event ignored: {}",
                   event.getEventId()
           );
           return;
       }

        log.info(
                "Received OrderCreatedEvent: orderId={}, userId={}, totalAmount={}",
                event.getOrderId(),
                event.getUserId(),
                event.getTotalAmount()
        );

     /*  processedMessageRepository.save(
               new ProcessedMessage(event.getEventId())
       );*/
       log.info("Processed event successfully: {}", event.getEventId());
    }
}