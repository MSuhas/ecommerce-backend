package com.suhasm.ecommerce.repository;

import com.suhasm.ecommerce.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findByPublishedAtIsNullOrderByIdAsc();

    @Query(value = """
    SELECT *
    FROM outbox_event
    WHERE
        status = 'PENDING'
        OR (
            status = 'CLAIMED'
            AND claimed_at < NOW() - INTERVAL '5 minutes'
        )
    ORDER BY id
    FOR UPDATE SKIP LOCKED
    LIMIT 100
    """, nativeQuery = true)
    List<OutboxEvent> findEventsToClaim();
}