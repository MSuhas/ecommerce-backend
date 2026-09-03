package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.entity.OutboxEvent;
import com.suhasm.ecommerce.entity.OutboxStatus;
import com.suhasm.ecommerce.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxClaimService {

    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public List<OutboxEvent> claimPendingEvents() {
        List<OutboxEvent> outboxEvents = outboxEventRepository.findEventsToClaim();

        Instant now = Instant.now();

        for (OutboxEvent event: outboxEvents) {
            event.setStatus(OutboxStatus.CLAIMED);
            event.setClaimedAt(now);
        }
        return outboxEvents;
    }
}
