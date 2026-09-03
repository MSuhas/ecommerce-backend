package com.suhasm.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "processed_message",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_processed_message_event_id",
                        columnNames = "eventId")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProcessedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventId;

    @Column(nullable = false)
    private Instant processedAt;

    public ProcessedMessage(String eventId) {
        this.eventId = eventId;
        this.processedAt = Instant.now();
    }
}
