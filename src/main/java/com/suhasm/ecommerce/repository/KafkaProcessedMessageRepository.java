package com.suhasm.ecommerce.repository;

import com.suhasm.ecommerce.entity.KafkaProcessedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface KafkaProcessedMessageRepository
        extends JpaRepository<KafkaProcessedMessage, Long> {
    @Modifying
    @Query(value = """
    INSERT INTO kafka_processed_message
        (consumer_group, event_id, processed_at)
    VALUES
        (:consumerGroup, :eventId, :processedAt)
    ON CONFLICT (consumer_group, event_id) DO NOTHING
    """, nativeQuery = true)
    int claimEvent(
            @Param("consumerGroup") String consumerGroup,
            @Param("eventId") String eventId,
            @Param("processedAt") Instant processedAt
    );

}
