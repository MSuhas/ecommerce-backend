package com.suhasm.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "kafka_processed_message",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_kafka_processed_message_event_id",
                        columnNames = "event_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaProcessedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String consumerGroup;

    @Column(nullable = false, length = 100)
    private String eventId;

    @Column(nullable = false)
    private Instant processedAt;
}
