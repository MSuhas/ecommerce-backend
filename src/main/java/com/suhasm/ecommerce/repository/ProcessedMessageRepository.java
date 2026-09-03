package com.suhasm.ecommerce.repository;

import com.suhasm.ecommerce.entity.ProcessedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessedMessageRepository
        extends JpaRepository<ProcessedMessage, Long> {

    boolean existsByEventId(String eventId);

    @Modifying
    @Query(value = """
            INSERT INTO processed_message (event_id, processed_at)
            VALUES (:eventId, :processedAt)
            ON CONFLICT (event_id) DO NOTHING
            """, nativeQuery = true)
    int claimEvent(
            @Param("eventId") String eventId,
            @Param("processedAt") java.time.Instant processedAt
    );
}
